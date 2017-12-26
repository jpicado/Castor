/*
 * CastorLearner: Castor learning algorithm. Similar to ProGolem, but uses inclusion dependencies and some optimizations.
 * -Uses inclusion dependencies to be schema independent.
 * -Optimization:
 * --Reuse coverage testing: uses BottomUpEvaluator.
 * --If run on VoltDB, uses bottom-clause construction inside a stored procedure.
 * --Minimizes bottom-clauses.
 */
package castor.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.datastructure.Pair;
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.algorithms.clauseevaluation.BottomUpEvaluator;
import castor.algorithms.clauseevaluation.ClauseEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.algorithms.transformations.ClauseTransformations;
import castor.algorithms.transformations.Reducer;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;
import castor.utils.Formatter;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;

public class Golem implements Learner {
	
	private static Logger logger = Logger.getLogger(Golem.class);
	
	private Parameters parameters;
	private GenericDAO genericDAO;
	private BottomClauseConstructionDAO bottomClauseConstructionDAO;
	private CoverageEngine coverageEngine;
	private Random randomGenerator;
	private ClauseEvaluator evaluator;
	private BottomClauseGenerator saturator;

	public Golem(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseContructionDAO, BottomClauseGenerator saturator, CoverageEngine coverageEngine, Parameters parameters) {
		this.parameters = parameters;
		this.genericDAO = genericDAO;
		this.bottomClauseConstructionDAO = bottomClauseContructionDAO;
		this.coverageEngine = coverageEngine;
		this.randomGenerator = new Random(parameters.getRandomSeed());
		this.evaluator = new BottomUpEvaluator();
		this.saturator = saturator;
	}
	
	public Parameters getParameters() {
		return parameters;
	}
	
	/*
	 * Run learning algorithm
	 * Does not support globalDefinition parameter, but it is included as parameter to satisfy Learner interface.
	 */
	public List<ClauseInfo> learn(Schema schema, DataModel dataModel, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, boolean globalDefinition) {
		TimeWatch tw = TimeWatch.start();
		
		logger.info("Training positive examples: " + this.coverageEngine.getAllPosExamples().size());
		logger.info("Training negative examples: " + this.coverageEngine.getAllNegExamples().size());
		
		List<ClauseInfo> definition = new LinkedList<ClauseInfo>();
		
		// Call covering approach
		definition.addAll(this.learnUsingCovering(schema, dataModel, posExamplesRelation, negExamplesRelation, spNameTemplate, parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms(), parameters.getSample(), parameters.getBeam(), parameters.getReductionMethod()));
	
		// Get string representation of definition
		StringBuilder sb = new StringBuilder();
		sb.append("Definition learned:\n");
        for (ClauseInfo clauseInfo : definition) {
        	// Count covered examples and include them in string representation
        	int posCoveredCount = 0;
        	int negCoveredCount = 0;
        	for (int i = 0; i < clauseInfo.getPosExamplesCovered().length; i++) {
				if (clauseInfo.getPosExamplesCovered()[i]) 
					posCoveredCount++;
			}
        	for (int i = 0; i < clauseInfo.getNegExamplesCovered().length; i++) {
				if (clauseInfo.getNegExamplesCovered()[i]) 
					negCoveredCount++;
			}
        	
			sb.append(Formatter.prettyPrint(clauseInfo.getClause())+"\t(Pos cover="+posCoveredCount+", Neg cover="+negCoveredCount+")\n");
		}
        logger.info(sb.toString());
        
        NumbersKeeper.learningTime += tw.time(TimeUnit.MILLISECONDS);
		
		return definition;
	}
	
	/*
	 * Evaluate the given definition using examples given in testCoverageEngine
	 */
	public EvaluationResult evaluate(CoverageEngine testCoverageEngine, Schema schema, List<ClauseInfo> definition, Relation testExamplesPos, Relation testExamplesNeg) {
		logger.info("Testing positive examples: " + testCoverageEngine.getAllPosExamples().size());
		logger.info("Testing negative examples: " + testCoverageEngine.getAllNegExamples().size());
		
		// ClauseInfo contains information about covered examples from training set
		// We must reset it to contain information about covered examples from testing set
		for (ClauseInfo clauseInfo : definition) {
			clauseInfo.resetCoveredExamples(testCoverageEngine.getAllPosExamples().size(), testCoverageEngine.getAllNegExamples().size());
		}
		
		// Compute positive and negative testing examples covered
		int totalPos = testCoverageEngine.getAllPosExamples().size();
		int totalNeg = testCoverageEngine.getAllNegExamples().size();

		int posCoveredCount = testCoverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, definition, testExamplesPos, true);
		int negCoveredCount = testCoverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, definition, testExamplesNeg, false);
		
		// Compute statistics
		int truePositive = posCoveredCount;
		int falsePositive = negCoveredCount;
		int trueNegative = totalNeg - negCoveredCount;
		int falseNegative = totalPos - posCoveredCount;
	
		// Print statistics
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t|\t    Actual\t\t|\n");
		sb.append("Predicted\t|    Positive\t|    Negative\t|    Total\n");
		sb.append("    Positive\t|\t"+(truePositive)+"\t|\t"+(falsePositive)+"\t|\t"+(truePositive+falsePositive)+"\n");
		sb.append("    Negative\t|\t"+(falseNegative)+"\t|\t"+(trueNegative)+"\t|\t"+(falseNegative+trueNegative)+"\n");
		sb.append("Total\t\t|\t"+(truePositive+falseNegative)+"\t|\t"+(falsePositive+trueNegative)+"\t|\t"+(totalPos+totalNeg)+"\n\n");
		
		double accuracy = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.ACCURACY, truePositive, falsePositive, trueNegative, falseNegative);
		double precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive, falsePositive, trueNegative, falseNegative);
		double recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositive, falsePositive, trueNegative, falseNegative);
		double f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive, trueNegative, falseNegative);

		sb.append("Accuracy: " + accuracy + "\n");
		sb.append("Precision: " + precision + "\n");
		sb.append("Recall: " + recall + "\n");
		sb.append("F1: " + f1 + "\n");
		
		logger.info("Statistics:\n" + sb.toString());
		
		EvaluationResult result = new EvaluationResult(accuracy, precision, recall, f1);
		return result;
	}
	
	/*
	 * Learn a clause using covering approach
	 */
	private List<ClauseInfo> learnUsingCovering(Schema schema, DataModel dataModel, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int maxRecall, int maxterms, int sampleSize, int beamWidth, String reductionMethod) {
		List<ClauseInfo> definition = new LinkedList<ClauseInfo>();
		
		// Get all positive examples from database and keep them in memory
		List<Tuple> remainingPosExamples = new LinkedList<Tuple>(coverageEngine.getAllPosExamples());
		
		int iterationsCounter = remainingPosExamples.size();
		while (remainingPosExamples.size() > 0 && iterationsCounter >= 0) {
			logger.info("Remaining uncovered examples: " + remainingPosExamples.size() + ", remaining iterations: " + iterationsCounter);
			
			// Learn clause
			ClauseInfo clauseInfo = learnClause(schema, dataModel, remainingPosExamples, posExamplesRelation, negExamplesRelation, spNameTemplate, iterations, maxRecall, maxterms, sampleSize, beamWidth);
			
			// If clauseInfo is null, there are no more good clauses, so exit
			if (clauseInfo == null) {
				break;
			}
			
			// Get new positive examples covered
			// Adding 1 to count seed example
			int newPosTotal = remainingPosExamples.size() + 1;
			int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true) + 1;
			
			// Get total positive examples covered
			int allPosTotal = coverageEngine.getAllPosExamples().size();
			int allPosCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, posExamplesRelation, true);
			
			// Get total negative examples covered
			int allNegTotal = coverageEngine.getAllNegExamples().size();
			int allNegCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
			
			// Compute statistics
			// For remaining positive examples
			int truePositive = newPosCoveredCount;
			int falsePositive = allNegCoveredCount;
			int trueNegative = allNegTotal - allNegCoveredCount;
			int falseNegative = newPosTotal - newPosCoveredCount;
			// For all examples
			int truePositiveAll = allPosCoveredCount;
			int falsePositiveAll = allNegCoveredCount;//totalPos - posCoveredCount;
			int trueNegativeAll = allNegTotal - allNegCoveredCount;
			int falseNegativeAll = allPosTotal - allPosCoveredCount;
			
			// Precision and F1 over new (uncovered) examples
			double precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive, falsePositive, trueNegative, falseNegative);
			double f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive, trueNegative, falseNegative);
			// Recall over all examples
			double recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositiveAll, falsePositiveAll, trueNegativeAll, falseNegativeAll);
		
			// Adding 1 to count seed example
			double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo) + 1;
		
			logger.info("Stats: Score=" + score + ", Precision(new)="+precision+", F1(new)="+f1+", Recall(all)="+recall);
			
			if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount, precision, recall)) {
				// Add clause to definition
				definition.add(clauseInfo);
				logger.info("New clause added to theory:\n" + Formatter.prettyPrint(clauseInfo.getClause()));
				logger.info("New pos cover = " + newPosCoveredCount + ", Total pos cover = " + allPosCoveredCount + ", Total neg cover = " + allNegCoveredCount);
				
				// Remove covered positive examples
				List<Tuple> coveredExamples = this.coverageEngine.coveredExamplesTuplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true);
				remainingPosExamples.removeAll(coveredExamples);
				iterationsCounter -= coveredExamples.size();
			} else {
				iterationsCounter--;
			}
		}
		
		return definition;
	}
	
	/*
	 * Check if given the numbers, the minimum conditions are satisfied
	 */
	private boolean satisfiesConditions(int truePositive, int falsePositive, int trueNegative, int falseNegative, int newPosCoveredCount, double precision, double recall) {
		boolean satisfiesPrecision = true;
		if (precision < parameters.getMinPrecision())
			satisfiesPrecision = false;
		
		boolean satisfiesRecall = true;
		if (recall < parameters.getMinRecall())
			satisfiesRecall = false;
		
		boolean satisfiesMinPos = true;
		if (newPosCoveredCount < parameters.getMinPos())
			satisfiesMinPos = false;
		
		double noise = 1.0 - precision;
		boolean satisfiesNoise = true;
		if (noise > parameters.getMaxNoise())
			satisfiesNoise = false;
		
		boolean satisfiesConditions = false;
		if(satisfiesPrecision && satisfiesRecall && satisfiesMinPos && satisfiesNoise) {
			satisfiesConditions = true;
		}
		return satisfiesConditions;
	}
	
	private ClauseInfo learnClause(Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int recall, int maxterms, int sampleSize, int beamWidth) {
		TimeWatch tw = TimeWatch.start();
		// Create local copy of remaining positive examples
		List<Tuple> localPosExamples = new LinkedList<Tuple>(remainingPosExamples);
		
		// Create candidate clauses by doing RLGG of pairs of examples
		List<ClauseInfo> candidates = this.generateCandidateClauses(schema, dataModel, localPosExamples, posExamplesRelation, negExamplesRelation);
		
		ClauseInfo bestClauseInfo = null;
		double bestScore = -1;
		double previousBestScore = -2;
		while (!candidates.isEmpty() && bestScore > previousBestScore) {
			logger.info("Current # of candidates: "+candidates.size());
			previousBestScore = bestScore;
			
			// Find clause with best score among candidates
			for (ClauseInfo clauseInfo : candidates) {
				double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo);
				if (score > bestScore) {
					bestClauseInfo = clauseInfo;
					bestScore = score;
				}
			}
			
			// Remove covered positive examples
			List<Tuple> coveredExamples = this.coverageEngine.coveredExamplesTuplesFromList(genericDAO, schema, bestClauseInfo, localPosExamples, posExamplesRelation, true);
			localPosExamples.removeAll(coveredExamples);
			
			// Generate new candidate clauses using LGG between bestClause and other pos examples
			logger.info("Best candidate has "+bestClauseInfo.getClause().getNumberLiterals() + " literals");
			candidates = this.generateCandidateClausesFromClause(bestClauseInfo, schema, dataModel, localPosExamples, posExamplesRelation, negExamplesRelation);
		}
		
		NumbersKeeper.learnClauseTime += tw.time();
		
		return bestClauseInfo;
	}
	
	private List<ClauseInfo> generateCandidateClauses(Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		
		// Get sample of pairs
		List<Pair<Tuple,Tuple>> pairs = this.selectRandomExamplePairs(remainingPosExamples, this.parameters.getSample());
		
		for (Pair<Tuple, Tuple> pair : pairs) {
			// Saturate examples
			MyClause clause1 = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, pair.getFirst(), schema, dataModel, parameters);
//			System.out.println(clause1.getNumberLiterals());
			MyClause clause2 = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, pair.getSecond(), schema, dataModel, parameters);
//			System.out.println(clause2.getNumberLiterals());
			
			// Generalize
//			System.out.println("Generalizing...");
			MyClause newClause = generalize(clause1, clause2, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, Reducer.MEASURE.CONSISTENCY);
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
//			System.out.println("Done.");
			// Add clause if it satisfies minimum conditions
			if (candidateSatisfiesConditions(newClauseInfo, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation)) {
				newClauses.add(newClauseInfo);
			}
		}
		return newClauses;
	}
	
	private List<ClauseInfo> generateCandidateClausesFromClause(ClauseInfo clauseInfo, Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		
		// Get sample
		List<Tuple> sample = this.selectRandomExamples(remainingPosExamples, this.parameters.getSample());
		
		for (Tuple example : sample) {
			// Saturate example
			MyClause candidate = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, example, schema, dataModel, parameters);
			
			// Generalize
			MyClause newClause = generalize(clauseInfo.getClause(), candidate, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, Reducer.MEASURE.CONSISTENCY);
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			// Create new clauseInfo, reusing coverage information from previous clause (new clause is generalization of old clause)
//			ClauseInfo newClauseInfo = new ClauseInfo(newClause, clauseInfo.getPosExamplesCovered(), clauseInfo.getNegExamplesCovered(), clauseInfo.getPosExamplesEvaluated(), clauseInfo.getNegExamplesEvaluated());
			
			// Add clause if it satisfies minimum conditions
			if (candidateSatisfiesConditions(newClauseInfo, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation)) {
				newClauses.add(newClauseInfo);
			}
			
			if (newClauses.size() >= parameters.getSample())
				break;
		}
		return newClauses;
	}
	
	private List<Pair<Tuple,Tuple>> selectRandomExamplePairs(List<Tuple> examples, int sampleSize) {
		List<Pair<Tuple,Tuple>> sample = new LinkedList<Pair<Tuple,Tuple>>();
		for (int i = 0; i < sampleSize; i++) {
			Tuple example1 = examples.get(randomGenerator.nextInt(examples.size()));
			Tuple example2 = examples.get(randomGenerator.nextInt(examples.size()));
			sample.add(new Pair<Tuple,Tuple>(example1, example2));
		}
		return sample;
	}
	
	/*
	 * Select K random examples from the given relation
	 */
	private List<Tuple> selectRandomExamples(List<Tuple> examples, int sampleSize) {
		List<Tuple> sample = new LinkedList<Tuple>();
		Set<Integer> randomPositions = generateListOfRandomNumbers(examples.size(), sampleSize);
		for (Integer pos : randomPositions) {
			sample.add(examples.get(pos));
		}
		return sample;
	}
	
	/*
	 * Generate a list of random numbers of the given size
	 */
	private Set<Integer> generateListOfRandomNumbers(int max, int size) {
		Set<Integer> generated = new TreeSet<Integer>();
		if (size > max) {
			for (int i = 0; i < max; i++) {
				generated.add(i);
			}
		} else {
			while (generated.size() < size) {
			    Integer next = randomGenerator.nextInt(max);
			    generated.add(next);
			}
		}
		return generated;
	}
	
	private List<ClauseInfo> generateCandidateClausesExhaustSample(Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		
		// Generate random sequence of indexes to access examples
		List<Integer> indexes1 = generateSequence(remainingPosExamples.size());
		List<Integer> indexes2 = generateSequence(remainingPosExamples.size());
		Collections.shuffle(indexes1, randomGenerator);
		Collections.shuffle(indexes2, randomGenerator);
		
		for (int i = 0; i < indexes1.size(); i++) {
			for (int j = 0; j < indexes2.size(); j++) {
				if (i != j) {
					// Get random examples
					Tuple example1 = remainingPosExamples.get(indexes1.get(i));
					Tuple example2 = remainingPosExamples.get(indexes2.get(j));
					
					// Saturate examples
					MyClause clause1 = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, example1, schema, dataModel, parameters);
					MyClause clause2 = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, example2, schema, dataModel, parameters);;
					
					// Generalize
					MyClause newClause = generalize(clause1, clause2, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, Reducer.MEASURE.CONSISTENCY);
					ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
					
					// Add clause if it satisfies minimum conditions
					if (candidateSatisfiesConditions(newClauseInfo, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation)) {
						newClauses.add(newClauseInfo);
					}
					
					if (newClauses.size() >= parameters.getSample())
						break;
				}
			}
			
			if (newClauses.size() >= parameters.getSample())
				break;
		}
		
		return newClauses;
	}
	
	private List<ClauseInfo> generateCandidateClausesFromClauseExhaustSample(ClauseInfo clauseInfo, Schema schema, DataModel dataModel, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		
		// Generate random sequence of indexes to access examples
		List<Integer> indexes = generateSequence(remainingPosExamples.size());
		Collections.shuffle(indexes, randomGenerator);
		
		for (int i = 0; i < indexes.size(); i++) {
			// Get random example
			Tuple example = remainingPosExamples.get(indexes.get(i));
			
			// Saturate example
			System.out.println("Generating bottom clause...");
			MyClause candidate = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, example, schema, dataModel, parameters);;
			
			// Generalize
			System.out.println("Generalizing...");
			MyClause newClause = generalize(clauseInfo.getClause(), candidate, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, Reducer.MEASURE.CONSISTENCY);
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			// Create new clauseInfo, reusing coverage information from previous clause (new clause is generalization of old clause)
//			ClauseInfo newClauseInfo = new ClauseInfo(newClause, clauseInfo.getPosExamplesCovered(), clauseInfo.getNegExamplesCovered(), clauseInfo.getPosExamplesEvaluated(), clauseInfo.getNegExamplesEvaluated());
			
			// Add clause if it satisfies minimum conditions
			System.out.println("Checking conditions...");
			if (candidateSatisfiesConditions(newClauseInfo, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation)) {
				newClauses.add(newClauseInfo);
			}
			
			if (newClauses.size() >= parameters.getSample())
				break;
		}
		
		return newClauses;
	}
	
	/*
	 * Generate List containing sequence of numbers
	 */
	private List<Integer> generateSequence(int n) {
		List<Integer> array = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			array.add(i);
		}
		return array;
	}
	
	/*
	 * Check conditions for potential candidates
	 * Currently, only checking minimum precision
	 */
	private boolean candidateSatisfiesConditions(ClauseInfo clauseInfo, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		// Get new positive examples covered
		// Adding 1 to count seed example
		int newPosTotal = remainingPosExamples.size() + 1;
		int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true) + 1;
		
		// Get total negative examples covered
		int totalNeg = coverageEngine.getAllNegExamples().size();
		int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
		
		// Compute statistics
		int truePositive = newPosCoveredCount;
		int falsePositive = negCoveredCount;
		int trueNegative = totalNeg - negCoveredCount;
		int falseNegative = newPosTotal - newPosCoveredCount;
		
		double precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive, falsePositive, trueNegative, falseNegative);
		
		boolean satisfiesPrecision = true;
		if (precision < parameters.getMinPrecision())
			satisfiesPrecision = false;
		
		boolean satisfiesConditions = false;
		if(satisfiesPrecision) {
			satisfiesConditions = true;
		}
		return satisfiesConditions;
	}
	
	private MyClause generalize(MyClause clause1, MyClause clause2, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, Reducer.MEASURE measure) {
		MyClause clause = lgg(clause1, clause2);
		clause = ClauseTransformations.minimize(clause);
		clause = Reducer.negativeReduce(genericDAO, coverageEngine, clause, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, measure, evaluator);
		return clause;
	}

	private MyClause lgg(MyClause clause1, MyClause clause2) {
		TimeWatch tw = TimeWatch.start();
		List<Literal> literals = new LinkedList<Literal>();
		
		// Find maximum variable name in clauses
		int maxVar = Commons.getMaxVarValue(clause1);
		maxVar = Math.max(maxVar, Commons.getMaxVarValue(clause2));
		maxVar += 1;
		
		Map<String, String> variableMap = new HashMap<String, String>();
		for (Literal literal1 : clause1.getLiterals()) {
			for (Literal literal2 : clause2.getLiterals()) {
				Literal newLiteral = lgg(literal1, literal2, variableMap, maxVar);
				if (newLiteral != null)
					literals.add(newLiteral);
			}
		}
		MyClause newClause = new MyClause(literals);
		NumbersKeeper.lggTime += tw.time();
		return newClause;
	}

	private Literal lgg(Literal literal1, Literal literal2, Map<String, String> variableMap, int maxVar) {
		Literal literal;
		if ( (literal1.isPositiveLiteral() && literal2.isNegativeLiteral()) || (literal1.isNegativeLiteral() && literal2.isPositiveLiteral()) ) {
			literal = null;
		} else {
			AtomicSentence atom = lgg(literal1.getAtomicSentence(), literal2.getAtomicSentence(), variableMap, maxVar);
			if (atom == null) {
				literal = null;
			} else {
				boolean isNegative = literal1.isNegativeLiteral();
				literal = new Literal(atom, isNegative);
			}
		}
		return literal;
	}

	private AtomicSentence lgg(AtomicSentence atom1, AtomicSentence atom2, Map<String, String> variableMap, int maxVar) {
		AtomicSentence atom;
		if (!atom1.getSymbolicName().equals(atom2.getSymbolicName()) ||
				atom1.getArgs().size() != atom2.getArgs().size()) {
			// Undefined
			atom = null;
		} else {
			List<Term> terms = new ArrayList<Term>();
			for (int i=0; i < atom1.getArgs().size(); i++) {
				Term term = lgg(atom1.getArgs().get(i), atom2.getArgs().get(i), variableMap, maxVar);
				terms.add(term);
			}
			atom = new Predicate(atom1.getSymbolicName(), terms);
		}
		return atom;
	}

	private Term lgg(Term term1, Term term2, Map<String, String> variableMap, int maxVar) {
		Term term;
		if (term1.equals(term2)) {
			term = term1;
		} else {
			// Compute key by ordering names
			String key;
			if (!Commons.isVariable(term1) && !Commons.isVariable(term2)) {
				// If both are constants, use order in which they're coming
				key = term1.getSymbolicName()+"_"+term2.getSymbolicName();
			} else {
				// If two vars, or constant and var, set lexicographic order
				if (term1.getSymbolicName().compareTo(term2.getSymbolicName()) < 0) {
					key = term1.getSymbolicName()+"_"+term2.getSymbolicName(); 
				} else {
					key = term2.getSymbolicName()+"_"+term1.getSymbolicName();
				}
			}
			// Check if variable already exists or create new one	
			String newTermSymbol;
			if (variableMap.containsKey(key)) {
				newTermSymbol = variableMap.get(key);
			} else {
				int realMaxVar = maxVar + variableMap.keySet().size();
				newTermSymbol = Commons.newVariable(realMaxVar);
				variableMap.put(key, newTermSymbol);
			}
			
			term = new Variable(newTermSymbol);
		}
		return term;
	}
		
	/*
	 * Compute score of a clause
	 */
	private double computeScore(Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo) {
		double score;
		if (clauseInfo.getScore() == null) {
			score = evaluator.computeScore(genericDAO, coverageEngine, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo, EvaluationFunctions.FUNCTION.COVERAGE);
		} else {
			score = clauseInfo.getScore();
		}
		return score;
	}
}
