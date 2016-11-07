/*
 * CastorLearner: Castor learning algorithm. Similar to ProGolem, but uses inclusion dependencies and some optimizations.
 * -Uses inclusion dependencies to be schema independent.
 * -Optimization:
 * --Reuse coverage testing: uses BottomUpEvaluator.
 * --If run on VoltDB, uses bottom-clause construction inside a stored procedure.
 * --Minimizes bottom-clauses.
 */
package castor.algorithms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.clauseevaluation.BottomUpEvaluator;
import castor.algorithms.clauseevaluation.ClauseEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.algorithms.transformations.CastorReducer;
import castor.algorithms.transformations.ClauseTransformations;
import castor.algorithms.transformations.DataDependenciesUtils;
import castor.algorithms.transformations.ReductionMethods;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.Parameters;
import castor.utils.Formatter;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;

public class CastorLearner {
	
	private static Logger logger = Logger.getLogger(CastorLearner.class);
	
	private Parameters parameters;
	private GenericDAO genericDAO;
	private BottomClauseConstructionDAO bottomClauseConstructionDAO;
	private CoverageEngine coverageEngine;
	private Random randomGenerator;
	private ClauseEvaluator evaluator;

	public CastorLearner(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseContructionDAO, CoverageEngine coverageEngine, Parameters parameters) {
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
		
		while (remainingPosExamples.size() > 0) {
			logger.info("Remaining uncovered examples: " + remainingPosExamples.size());
			
			// Compute best ARMG
			ClauseInfo clauseInfo = learnClause(schema, modeH, modesB, remainingPosExamples, posExamplesRelation, negExamplesRelation, spNameTemplate, iterations, maxRecall, maxterms, sampleSize, beamWidth);
			
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
			
			logger.info("Stats before reduction: Precision(new)="+precision+", F1(new)="+f1+", Recall(all)="+recall);
			
			// Reduce clause only if it satisfies conditions
			if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount, precision, recall)) {
				if (!reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_NONE)) {
					// Compute negative based reduction
					// Add 1 to scores to count seed example, which is not in remainingPosExamples
					double beforeReduceScore = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo) + 1;
					logger.info("Before reduction - NumLits:"+clauseInfo.getClause().getNumberLiterals()+", Score:"+beforeReduceScore);
					logger.debug("Before reduction:\n"+Formatter.prettyPrint(clauseInfo.getClause()));
					
					if (reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_CONSISTENCY)) {
						clauseInfo.setMoreGeneralClause(CastorReducer.negativeReduce(genericDAO, coverageEngine, clauseInfo.getClause(), schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, CastorReducer.MEASURE.CONSISTENCY, evaluator));
					} else if (reductionMethod.equals(ReductionMethods.NEGATIVE_REDUCTION_PRECISION)) {
						clauseInfo.setMoreGeneralClause(CastorReducer.negativeReduce(genericDAO, coverageEngine, clauseInfo.getClause(), schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, CastorReducer.MEASURE.PRECISION, evaluator));
					}
					
					double afterReduceScore = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo) + 1;
					logger.info("After reduction - NumLits:"+clauseInfo.getClause().getNumberLiterals()+", Score:"+afterReduceScore);
					logger.debug("After reduction:\n"+Formatter.prettyPrint(clauseInfo.getClause()));
				}
				
				// Final minimization to remove redundant literals
				clauseInfo.setMoreGeneralClause(this.transform(schema, clauseInfo.getClause()));
				logger.info("After minimization - NumLits:"+clauseInfo.getClause().getNumberLiterals());
				logger.debug("After minimization:\n"+ Formatter.prettyPrint(clauseInfo.getClause()));
				
				// Get new positive examples covered
				// Adding 1 to count seed example
				newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true) + 1;
				posCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, posExamplesRelation, true);

				// Compute statistics
				truePositive = newPosCoveredCount;
				falsePositive = negCoveredCount;
				trueNegative = totalNeg - negCoveredCount;
				falseNegative = newPosTotal - newPosCoveredCount;
				truePositiveAll = posCoveredCount;
				falsePositiveAll = totalPos = posCoveredCount;
				
				// Adding 1 to count seed example
				double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo) + 1;
				
				// Precision and F1 over new (uncovered) examples
				precision = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.PRECISION, truePositive, falsePositive, trueNegative, falseNegative);
				f1 = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.F1, truePositive, falsePositive, trueNegative, falseNegative);
				// Recall over all examples
				recall = EvaluationFunctions.score(EvaluationFunctions.FUNCTION.RECALL, truePositiveAll, falsePositiveAll, trueNegative, falseNegative);
				
				logger.info("Stats: Score=" + score + ", Precision(new)="+precision+", F1(new)="+f1+", Recall(all)="+recall);
				
				if (satisfiesConditions(truePositive, falsePositive, trueNegative, falseNegative, newPosCoveredCount, precision, recall)) {
					// Add clause to definition
					definition.add(clauseInfo);
					logger.info("New clause added to theory:\n" + Formatter.prettyPrint(clauseInfo.getClause()));
					logger.info("New pos cover = " + newPosCoveredCount + ", Total pos cover = " + posCoveredCount + ", Total neg cover = " + negCoveredCount);
					
					// Remove covered positive examples
					List<Tuple> coveredExamples = this.coverageEngine.coveredExamplesTuplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true);
					remainingPosExamples.removeAll(coveredExamples);
				}
			}// end condition
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
	
	/*
	 * Perform generalization using beam search + ARMG
	 */
	private ClauseInfo learnClause(Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int recall, int maxterms, int sampleSize, int beamWidth) {
		BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
		
		// First unseen positive example (pop)
		Tuple exampleTuple = remainingPosExamples.remove(0);
		
		// Generate bottom clause
		TimeWatch tw = TimeWatch.start();
		logger.info("Generating bottom clause for "+exampleTuple.getValues().toString()+"...");
		MyClause bottomClause = saturator.generateBottomClause(bottomClauseConstructionDAO, exampleTuple, spNameTemplate, iterations, recall, maxterms);
		logger.debug("Bottom clause: \n"+ Formatter.prettyPrint(bottomClause));
		logger.info("Literals: " + bottomClause.getNumberLiterals());
		logger.info("Saturation time: " + tw.time(TimeUnit.MILLISECONDS) + " milliseconds.");
		
		// MINIMIZATION CAN BE USEFUL WHEN THERE ARE NO ISSUES IF LITERALS WITH VARIABLES ARE REMOVED BECAUSE THERE ARE LITERALS WITH CONSTANTS.
		// IF LITERALS WITH VARIABLES ARE IMPORTANT (E.G. TO BE MORE GENERAL), MINIMIZATION SHOULD BE TURNED OFF.
		// Minimize bottom clause
		if (parameters.isMinimizeBottomClause()) {
			logger.info("Transforming bottom clause...");
			bottomClause = this.transform(schema, bottomClause);
			logger.info("Literals of transformed clause: " + bottomClause.getNumberLiterals());
			logger.debug("Transformed bottom clause:\n"+ Formatter.prettyPrint(bottomClause));
		}
		
		// Reorder bottom clause
		// NOTE: this is needed for some datasets (e.g. HIV-small, fold 2)
		logger.info("Reordering bottom clause...");
		bottomClause = this.reorderAccordingToINDs(schema, bottomClause);
		
		logger.info("Generalizing clause...");
		
		List<ClauseInfo> bestARMGs = new LinkedList<ClauseInfo>();
		bestARMGs.add(new ClauseInfo(bottomClause, this.coverageEngine.getAllPosExamples().size(), this.coverageEngine.getAllNegExamples().size()));
		
		boolean createdNewARMGS = true;
		int iters = 0;
		// Add 1 to scores to count seed example, which is not in remainingPosExamples
		double clauseScore = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, bestARMGs.get(0)) + 1;
		logger.info("Best armg at iter "+iters+" - NumLits:"+bestARMGs.get(0).getClause().getNumberLiterals()+", Score:"+clauseScore);
		
		// Generalize only if there are more examples
		if (remainingPosExamples.size() > 0) {
			while(createdNewARMGS) {
				iters++;
				createdNewARMGS = false;
				
				// Compute best score of clauses in bestARMGs
				double bestScore = Double.NEGATIVE_INFINITY;
				for (ClauseInfo clauseInfo : bestARMGs) {
					double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo);
					bestScore = Math.max(bestScore, score);
				}
				
				// Select K random examples
				List<Tuple> sample = selectRandomExamples(posExamplesRelation, sampleSize);
				
				// Generalize each clause in ARMG using examples in sample
				List<ClauseInfo> newARMGs = new LinkedList<ClauseInfo>();
				for (ClauseInfo clauseInfo : bestARMGs) {
					for (Tuple tuple : sample) {
						// Perform ARMG
						ClauseInfo newClauseInfo = armg(schema, clauseInfo, tuple, posExamplesRelation);
						
						// Keep clause only if its score is better tahn current best score
						double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, newClauseInfo);
						if (score > bestScore) {
							newARMGs.add(newClauseInfo);
						}
					}
				}
				
				if (!newARMGs.isEmpty()) {
					// Keep highest scoring N clauses from newARMGs
					bestARMGs.clear();
					bestARMGs.addAll(this.getHighestScoring(newARMGs, beamWidth, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation));
					createdNewARMGS = true;
				}
				
				clauseScore = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, bestARMGs.get(0)) + 1;
				logger.info("Best armg at iter "+iters+" - NumLits:"+bestARMGs.get(0).getClause().getNumberLiterals()+", Score:"+clauseScore);
			}
		}
		
		// Get highest scoring clause from bestARMGs
		ClauseInfo bestClauseInfo = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (ClauseInfo clauseInfo : bestARMGs) {
			double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clauseInfo);
			if (score > bestScore) {
				bestClauseInfo = clauseInfo;
				bestScore = score;
			}
		}
		
		return bestClauseInfo;
	}
	
	/*
	 * Reorder clause so that all literals in same inclusion chain are together
	 */
	private MyClause reorderAccordingToINDs(Schema schema, MyClause clause) {
		List<Literal> newLiterals = new LinkedList<Literal>();

		for (Literal literal : clause.getNegativeLiterals()) {	
			if (!newLiterals.contains(literal)) {
				List<Literal> chainLiterals = DataDependenciesUtils.findLiteralsInInclusionChain(schema, clause, literal);
				
				// Add all literals
				newLiterals.addAll(chainLiterals);
			}			
		}
		
		// Add positive literals and create clause
		newLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newLiterals);
		return newClause;
	}
	
	/*
	 * Transform a clause: minimization, reordering, etc.
	 */
	private MyClause transform(Schema schema, MyClause clause) {
		// Minimize using theta-transformation 
		clause = ClauseTransformations.minimize(clause);
		// Reorder to delay cartesian products
		// IF REORDERED, NEGATIVE REDUCTION IS AFFECTED
		//clause = ClauseTransformations.reorder(clause);
		return clause;
	}
	
	/*
	 * Get a list of the N highest scoring clauses from a list of clauses, using the given evaluation function
	 */
	private List<ClauseInfo> getHighestScoring(List<ClauseInfo> clausesInfos, int beamWidth, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation) {
		List<ClauseInfo> bestClauses = new LinkedList<ClauseInfo>();
		Double[] clausesScores = new Double[clausesInfos.size()];
		
		if (beamWidth >= clausesInfos.size()) {
			// Return all clauses, order does not matter
			bestClauses.addAll(clausesInfos);
		} else {
			// Use selection score, stopping when beamWidth is reached
			for (int i = 0; i < beamWidth && i < clausesInfos.size() - 1; i++)
	        {
	            int index = i;
	            for (int j = i + 1; j < clausesInfos.size(); j++) {
	            	if (clausesScores[j] == null) {
	            		double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clausesInfos.get(j));
	            		clausesScores[j] = score;
	            	}
	            	if (clausesScores[index] == null) {
	            		double score = this.computeScore(schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, clausesInfos.get(index));
	            		clausesScores[index] = score;
	            	}
	                if (clausesScores[j] > clausesScores[index]) {
	                    index = j;
	                }
	            }
	      
	            // Switch positions for clauses and scores
	            ClauseInfo betterClause = clausesInfos.get(index);
	            Double betterClauseScore = clausesScores[index];
	            clausesInfos.set(index, clausesInfos.get(i));
	            clausesScores[index] = clausesScores[i];
	            clausesInfos.set(i, betterClause);
	            clausesScores[i] = betterClauseScore;
	            
	            bestClauses.add(betterClause);
	        }
		}
        return bestClauses;
    }
	
	/*
	 * Select K random examples from the given relation
	 */
	private List<Tuple> selectRandomExamples(Relation posExamplesRelation, int sampleSize) {
		List<Tuple> examples = new LinkedList<Tuple>();
		Set<Integer> randomPositions = generateListOfRandomNumbers(this.coverageEngine.getAllPosExamples().size(), sampleSize);
		for (Integer pos : randomPositions) {
			examples.add(this.coverageEngine.getAllPosExamples().get(pos));
		}
		return examples;
	}
	
	/*
	 * Generate a list of random numbers of the given size
	 */
	private Set<Integer> generateListOfRandomNumbers(int max, int size) {
		Set<Integer> generated = new TreeSet<Integer>();
		if (size > max) {
			for (int i = 0; i <= max; i++) {
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
	
	/*
	 * ARMG construction algorithm
	 * Assumes that the example tuple corresponds to a relation that matches the head predicate of the clause
	 */
	private ClauseInfo armg(Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation) {
		ClauseInfo newClauseInfo = new ClauseInfo(clauseInfo.getClause(), clauseInfo.getPosExamplesCovered().clone(), clauseInfo.getNegExamplesCovered().clone(), clauseInfo.getPosExamplesEvaluated().clone(), clauseInfo.getNegExamplesEvaluated().clone());
		while(!this.entails(genericDAO, coverageEngine, schema, newClauseInfo, exampleTuple, posExamplesRelation)) {
			// If body is empty and still example wasn't entailed, it means that head was restrictive (e.g. same variable for multiple attributes)
			if (newClauseInfo.getClause().getNumberNegativeLiterals() == 0) 
				break;
			int blockingLiteralPosition = findFirstBlockingLiteral(schema, newClauseInfo, exampleTuple, posExamplesRelation);
			
			MyClause newClause = removeLiteralAtPosition(newClauseInfo.getClause(), blockingLiteralPosition);
			newClause = removeLiteralsWithUnsatisfiedInds(schema, newClause);
			newClause = removeNotHeadConnectedLiterals(newClause);
			
			// Set more general clause (reuse information about already covered examples)
			newClauseInfo.setMoreGeneralClause(newClause);
		}
		return newClauseInfo;
	}
	
	/*
	 * Given clause C, first remove literal at specified position, then remove literals that are not head-connected
	 */
	private MyClause removeLiteralAtPosition(MyClause clause, int position) {
		// MyClause's list of literals is unmodifiable, so must create a new list
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		newClauseLiterals.addAll(clause.getNegativeLiterals());
		
		// Remove negative literal at position
		newClauseLiterals.remove(position);
		newClauseLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newClauseLiterals);
		
		return newClause;
	}
	
	/*
	 * Remove literals that are not head-conneted from clause
	 */
	private MyClause removeNotHeadConnectedLiterals(MyClause clause) {
		Set<String> seenVariables = new HashSet<String>();
		
		// Add head variables to seen variables
		for (Term term : clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs()) {
			seenVariables.add(term.getSymbolicName());
		}
		
		// MyClause's list of literals is unmodifiable, so must create a new list
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		newClauseLiterals.addAll(clause.getNegativeLiterals());
		
		// Iterate through negative literals and remove not head-connected from list
		Iterator<Literal> iterator = newClauseLiterals.iterator();
		while (iterator.hasNext()) {
		   Literal literal = iterator.next();

		   boolean headConnected = false;
		   Set<String> literalVariables = new HashSet<String>();
		   for (Term term : literal.getAtomicSentence().getArgs()) {
			   literalVariables.add(term.getSymbolicName());
			   if (seenVariables.contains(term.getSymbolicName())) {
				   // Literal is head-connected
				   headConnected = true;
			   }
		   }
		   
		   if (headConnected) {
			   // Literal is head-connected, so add its variables to seen variables
			   seenVariables.addAll(literalVariables);
		   } else {
			   // Remove literal because it is not head-connected
			   iterator.remove();
		   }
		}
		
		newClauseLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newClauseLiterals);
		
		return newClause;
	}
	
	/*
	 * Create a new clause where literals in original clause for which INDs are not satisfied are removed
	 */
	private MyClause removeLiteralsWithUnsatisfiedInds(Schema schema, MyClause clause) {
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		
		// For each literal, check if IND holds. If it does, add to list
		for (int i = 0; i < clause.getNegativeLiterals().size(); i++) {
			Literal literal = clause.getNegativeLiterals().get(i);
			String literalPredicateName = literal.getAtomicSentence().getSymbolicName();

			// If there are inclusion dependencies for the literal, only keep literal if all INDs hold
			if (schema.getInclusionDependencies().containsKey(literalPredicateName)) {
			
				boolean allIndsSatisfied = true;
				for (InclusionDependency ind : schema.getInclusionDependencies().get(literalPredicateName)) {
					Term leftIndTerm = literal.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
					
					// Check if IND is satisifed
					boolean indSatisfied = false;
					for (int j = 0; j < clause.getNegativeLiterals().size(); j++) {
						Literal otherLiteral = clause.getNegativeLiterals().get(j);
						String otherLiteralPredicateName = otherLiteral.getAtomicSentence().getSymbolicName();
						
						// Consider other literal only if:
						// 1) it's after current literal, or
						// 2) it's before current literal and it was kept in newClauseLiterals
						if (j > i || (j < i && newClauseLiterals.contains(otherLiteral))) {
							if (otherLiteralPredicateName.equals(ind.getRightPredicateName())) {
								Term rightIndTerm = otherLiteral.getAtomicSentence().getArgs().get(ind.getRightAttributeNumber());
		
								if (leftIndTerm.equals(rightIndTerm)) {
									// IND satisfied
									indSatisfied = true;
									break;
								}
							}
						}
					}
	
					// If IND is not satisfied, add literal to new clause
					if (!indSatisfied) {
						//newClauseLiterals.add(literal);
						allIndsSatisfied = false;
						break;
					}
				}
				if (allIndsSatisfied) {
					newClauseLiterals.add(literal);
				}
				
			} else {
				// If there are no inclusion dependencies for the literal, keep it
				newClauseLiterals.add(literal);
			}
		}

		newClauseLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newClauseLiterals);
		
		return newClause;
	}
	
	/*
	 * Given bottom clause C and example e, find position of first blocking literal of C w.r.t. e
	 */
	private int findFirstBlockingLiteral(Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation) {
		int lowerbound = 0;
		int upperbound = clauseInfo.getClause().getNumberNegativeLiterals() - 1;
		
		boolean[] originalPosExamplesCovered = clauseInfo.getPosExamplesCovered();
		boolean[] originalNegExamplesCovered = clauseInfo.getNegExamplesCovered();
		
		while(lowerbound != upperbound) {
			int n = (lowerbound + upperbound) / 2;
			
			// Create new clause with first n body literals from clause
			List<Literal> literals = new LinkedList<Literal>();
			literals.add(clauseInfo.getClause().getPositiveLiterals().get(0));
			for (int i = 0; i <= n; i++) {
				literals.add((clauseInfo.getClause().getNegativeLiterals().get(i)));
			}
			MyClause newClause = new MyClause(literals);
			ClauseInfo newClauseInfo = new ClauseInfo(
					newClause, 
					originalPosExamplesCovered.clone(), 
					originalNegExamplesCovered.clone(),
					new boolean[originalPosExamplesCovered.length],
					new boolean[originalNegExamplesCovered.length]);
			
			if (this.entails(genericDAO, coverageEngine, schema, newClauseInfo, exampleTuple, posExamplesRelation)) {
				lowerbound = n + 1;
			} else {
				upperbound = n;
			}			
		}
		
		return lowerbound;
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
	
	/*
	 * Check if a clause entials an example
	 */
	private boolean entails(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation) {
		return evaluator.entails(genericDAO, coverageEngine, schema, clauseInfo, exampleTuple, posExamplesRelation);
	}
}
