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
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.clauseevaluation.BottomUpEvaluator;
import castor.algorithms.clauseevaluation.ClauseEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.algorithms.transformations.ClauseTransformations;
import castor.algorithms.transformations.DataDependenciesUtils;
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
import castor.utils.TimeKeeper;
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
        
        TimeKeeper.learningTime += tw.time(TimeUnit.MILLISECONDS);
		
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
			ClauseInfo clauseInfo = beamSearchIteratedARMG(schema, modeH, modesB, remainingPosExamples, posExamplesRelation, negExamplesRelation, spNameTemplate, iterations, maxRecall, maxterms, sampleSize, beamWidth);
			
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
				List<Tuple> coveredExamples = this.coverageEngine.coveredExamplesTuplesFromRelation(genericDAO, schema, clauseInfo, posExamplesRelation, true);
				for (Tuple coveredExample : coveredExamples) {
					remainingPosExamples.remove(coveredExample);
				}
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
		
		
		
		return null;
	}
	
	private List<ClauseInfo> generateCandidateClauses(Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples) {
		BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
		
		for (int i = 0; i < remainingPosExamples.size(); i++) {
			for (int j = 0; j < remainingPosExamples.size(); j++) {
				if (i != j) {
					
				}
			}
		}
		
		int foundCandidates = 0;
		while(foundCandidates < 5) {
			int exampleIndex1 = randomGenerator.nextInt(remainingPosExamples.size());
			int exampleIndex2 = randomGenerator.nextInt(remainingPosExamples.size());
			
			if (exampleIndex1 != exampleIndex2) {
				Tuple example1 = remainingPosExamples.get(exampleIndex1);
				Tuple example2 = remainingPosExamples.get(exampleIndex2);
				
				MyClause clause1 = saturator.generateBottomClause(bottomClauseConstructionDAO, example1, dataModel.getSpName(), parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms());
				MyClause clause2 = saturator.generateBottomClause(bottomClauseConstructionDAO, example2, dataModel.getSpName(), parameters.getIterations(), parameters.getRecall(), parameters.getMaxterms());
				
				MyClause lgg = generalize(clause1, clause2);
			}
		}
		
		
		return null;
	}
	
	private MyClause generalize(MyClause clause1, MyClause clause2) {
		MyClause clause = lgg(clause1, clause2);
		clause = ClauseTransformations.minimize(clause);
		return null;
	}

	private MyClause lgg(MyClause clause1, MyClause clause2) {
		List<Literal> literals = new LinkedList<Literal>();
		
		Map<String, String> variableMap = new HashMap<String, String>();
		for (Literal literal1 : clause1.getLiterals()) {
			for (Literal literal2 : clause2.getLiterals()) {
				Literal newLiteral = lgg(literal1, literal2, variableMap);
				if (newLiteral != null)
					literals.add(newLiteral);
			}
		}
		MyClause newClause = new MyClause(literals);
		return newClause;
	}

	private Literal lgg(Literal literal1, Literal literal2, Map<String, String> variableMap) {
		Literal literal;
		if ( (literal1.isPositiveLiteral() && literal2.isNegativeLiteral()) || (literal1.isNegativeLiteral() && literal2.isPositiveLiteral()) ) {
			literal = null;
		} else {
			AtomicSentence atom = lgg(literal1.getAtomicSentence(), literal2.getAtomicSentence(), variableMap);
			if (atom == null) {
				literal = null;
			} else {
				boolean isNegative = false;
				if (literal1.isNegativeLiteral())
					isNegative = true;
				literal = new Literal(atom, isNegative);
			}
		}
		return literal;
	}

	private AtomicSentence lgg(AtomicSentence atom1, AtomicSentence atom2, Map<String, String> variableMap) {
		AtomicSentence atom;
		if (!atom1.getSymbolicName().equals(atom2.getSymbolicName())) {
			// Undefined
			atom = null;
		} else {
			List<Term> terms = new ArrayList<Term>();
			for (int i=0; i<atom1.getArgs().size(); i++) {
				Term term = lgg(atom1.getArgs().get(i), atom2.getArgs().get(i), variableMap);
				terms.add(term);
			}
			atom = new Predicate(atom1.getSymbolicName(), terms);
		}
		return atom;
	}

	private Term lgg(Term term1, Term term2, Map<String, String> variableMap) {
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
				//newTermSymbol = "v_"+key;
				newTermSymbol = "v_"+variableCounter;
				variableCounter++;
				variableMap.put(key, newTermSymbol);
			}
			
			term = new Variable(newTermSymbol);
		}
		return term;
	}

	/*
	 * Perform generalization using beam search + ARMG
	 */
	private ClauseInfo beamSearchIteratedARMG(Schema schema, Mode modeH, List<Mode> modesB, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, int iterations, int recall, int maxterms, int sampleSize, int beamWidth) {
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
		TimeWatch tw = TimeWatch.start();
		// Minimize using theta-transformation 
		clause = ClauseTransformations.minimize(clause);
		// Reorder to delay cartesian products
		// IF REORDERED, NEGATIVE REDUCTION IS AFFECTED
		//clause = ClauseTransformations.reorder(clause);
		TimeKeeper.transformationTime += tw.time();
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
