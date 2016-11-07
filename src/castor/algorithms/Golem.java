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
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.clauseevaluation.BottomUpEvaluator;
import castor.algorithms.clauseevaluation.ClauseEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.algorithms.transformations.ClauseTransformations;
import castor.algorithms.transformations.Reducer;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Mode;
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

public class Golem {
	
	private static Logger logger = Logger.getLogger(Golem.class);
	
	private Parameters parameters;
	private DataModel dataModel;
	private GenericDAO genericDAO;
	private BottomClauseConstructionDAO bottomClauseConstructionDAO;
	private CoverageEngine coverageEngine;
	private Random randomGenerator;
	private ClauseEvaluator evaluator;

	public Golem(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseContructionDAO, CoverageEngine coverageEngine, DataModel dataModel, Parameters parameters) {
		this.dataModel = dataModel;
		this.parameters = parameters;
		this.genericDAO = genericDAO;
		this.bottomClauseConstructionDAO = bottomClauseContructionDAO;
		this.coverageEngine = coverageEngine;
		this.randomGenerator = new Random(parameters.getRandomSeed());
		this.evaluator = new BottomUpEvaluator();
	}
	
	public Parameters getParameters() {
		return parameters;
	}
	
	/*
	 * Run learning algorithm
	 */
	public List<ClauseInfo> learn(Schema schema, Mode modeH, List<Mode> modesB, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate) {
		TimeWatch tw = TimeWatch.start();
		
		List<ClauseInfo> definition = new LinkedList<ClauseInfo>();
		
		// Call covering approach
		definition.addAll(this.learnUsingCovering(schema, modeH, modesB, posExamplesRelation, negExamplesRelation, spNameTemplate, parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms(), parameters.getSample(), parameters.getBeam(), parameters.getReductionMethod()));
	
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
        
        // Evaluate on training data
        logger.info("Evaluating on training data...");
        this.evaluate(this.coverageEngine, schema, definition, posExamplesRelation, negExamplesRelation);
        
        NumbersKeeper.learningTime += tw.time(TimeUnit.MILLISECONDS);
		
		return definition;
	}
	
	/*
	 * Evaluate the given definition using examples given in testCoverageEngine
	 */
	public EvaluationResult evaluate(CoverageEngine testCoverageEngine, Schema schema, List<ClauseInfo> definition, Relation testExamplesPos, Relation testExamplesNeg) {
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
	private List<ClauseInfo> learnUsingCovering(Schema schema, Mode modeH, List<Mode> modesB, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int maxRecall, int maxterms, int sampleSize, int beamWidth, String reductionMethod) {
		List<ClauseInfo> definition = new LinkedList<ClauseInfo>();
		
		// Get all positive examples from database and keep them in memory
		List<Tuple> remainingPosExamples = new LinkedList<Tuple>(coverageEngine.getAllPosExamples());
		
		int iterationsCounter = remainingPosExamples.size();
		while (remainingPosExamples.size() > 0 && iterationsCounter >= 0) {
			logger.info("Remaining uncovered examples: " + remainingPosExamples.size() + ", remaining iterations: " + iterationsCounter);
			
			// Learn clause
			ClauseInfo clauseInfo = learnClause(schema, modeH, modesB, remainingPosExamples, posExamplesRelation, negExamplesRelation, spNameTemplate, iterations, maxRecall, maxterms, sampleSize, beamWidth);
			
			// If clauseInfo is null, there are no more good clauses, so exit
			if (clauseInfo == null) {
				break;
			}
			
			// Get new positive examples covered
			// Adding 1 to count seed example
			int newPosTotal = remainingPosExamples.size() + 1;
			int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true) + 1;
			
			// Get total positive examples covered
			int totalPos = coverageEngine.getAllPosExamples().size();
			int posCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, posExamplesRelation, true);
			
			// Get total negative examples covered
			int totalNeg = coverageEngine.getAllNegExamples().size();
			int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
			
			// Compute statistics
			int truePositive = newPosCoveredCount;
			int falsePositive = negCoveredCount;
			int trueNegative = totalNeg - negCoveredCount;
			int falseNegative = newPosTotal - newPosCoveredCount;
			int truePositiveAll = posCoveredCount;
			int falsePositiveAll = totalPos - posCoveredCount;
			
			// Precision and F1 over new (uncovered) examples
			double precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive, falsePositive, trueNegative, falseNegative);
			double f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive, trueNegative, falseNegative);
			// Recall over all examples
			double recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositiveAll, falsePositiveAll, trueNegative, falseNegative);
		
			// Adding 1 to count seed example
			double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo) + 1;
		
			logger.info("Stats: Score=" + score + ", Precision(new)="+precision+", F1(new)="+f1+", Recall(all)="+recall);
			
			if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount, precision, recall)) {
				// Add clause to definition
				definition.add(clauseInfo);
				logger.info("New clause added to theory:\n" + Formatter.prettyPrint(clauseInfo.getClause()));
				logger.info("New pos cover = " + newPosCoveredCount + ", Total pos cover = " + posCoveredCount + ", Total neg cover = " + negCoveredCount);
				
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
	
	private ClauseInfo learnClause(Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int recall, int maxterms, int sampleSize, int beamWidth) {
		// Create local copy of remaining positive examples
		List<Tuple> localPosExamples = new LinkedList<Tuple>(remainingPosExamples);
		
		// Create candidate clauses by doing RLGG of pairs ef examples
		List<ClauseInfo> candidates = this.generateCandidateClauses(schema, modeH, modesB, localPosExamples, posExamplesRelation, negExamplesRelation);
		
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
			candidates = this.generateCandidateClausesFromClause(bestClauseInfo, schema, modeH, modesB, localPosExamples, posExamplesRelation, negExamplesRelation);
		}
		
		return bestClauseInfo;
	}
	
	private List<ClauseInfo> generateCandidateClauses(Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
		
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
					MyClause clause1 = saturator.generateGroundBottomClause(bottomClauseConstructionDAO, example1, dataModel.getSpName(), parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms());
					MyClause clause2 = saturator.generateGroundBottomClause(bottomClauseConstructionDAO, example2, dataModel.getSpName(), parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms());
					
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
	
	private List<ClauseInfo> generateCandidateClausesFromClause(ClauseInfo clauseInfo, Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> newClauses = new LinkedList<ClauseInfo>();
		BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
		
		// Generate random sequence of indexes to access examples
		List<Integer> indexes = generateSequence(remainingPosExamples.size());
		Collections.shuffle(indexes, randomGenerator);
		
		for (int i = 0; i < indexes.size(); i++) {
			// Get random example
			Tuple example = remainingPosExamples.get(indexes.get(i));
			
			// Saturate example
			MyClause candidate = saturator.generateGroundBottomClause(bottomClauseConstructionDAO, example, dataModel.getSpName(), parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms());
			
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
//		clause = Reducer.negativeReduce(genericDAO, coverageEngine, clause, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, measure, evaluator);
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
