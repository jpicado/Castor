/*
 * Computes scores and entailment of a given clause
 * Optimizations:
 * -Keeps string representation of evaluated clause, to reuse coverage information
 * -Bottom-up coverage optimization: if clause C covers e, then clause C', which is more general than C, also covers e
 */

package castor.algorithms.clauseevaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

public class BottomUpEvaluator implements ClauseEvaluator {

	private Map<String, ExamplesInfo> cachedClauses;
	
	public BottomUpEvaluator() {
		super();
		cachedClauses = new HashMap<String,ExamplesInfo>();
	}

	public double computeScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();
		
		// Get string representation
		String clauseString = clauseToString(clauseInfo.getClause());
		
		// Get examples info if existing, if not create new
		ExamplesInfo examplesInfo;
		if (!cachedClauses.containsKey(clauseString)) {
			examplesInfo = new ExamplesInfo();
		}
		else {
			examplesInfo = cachedClauses.get(clauseString);
		}
		
		// Update evaluated and covered examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getPosExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getPosExamplesEvaluated()[i] = true;
					
					// Update covered
					if (examplesInfo.getPosExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getPosExamplesCovered()[i] = true;
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getNegExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getNegExamplesEvaluated()[i] = true;
					
					// Update covered
					if (examplesInfo.getNegExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getNegExamplesCovered()[i] = true;
					}
				}
			}
		}
		
		// Get new positive examples covered
		int newPosTotal = remainingPosExamples.size();
		int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, remainingPosExamples, posExamplesRelation, true);

		// Get total negative examples covered
		int totalNeg = coverageEngine.getAllNegExamples().size();
		int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
		
		// Compute statistics
		int truePositive = newPosCoveredCount;
		int falsePositive = negCoveredCount;
		int trueNegative = totalNeg - negCoveredCount;
		int falseNegative = newPosTotal - newPosCoveredCount;
		
		// Compute score
		double score = EvaluationFunctions.score(evaluationFunction, truePositive, falsePositive, trueNegative, falseNegative);
		clauseInfo.setScore(score);
		
		// Store information about covered and evaluated examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (clauseInfo.getPosExamplesEvaluated()[i]) {
				if (clauseInfo.getPosExamplesCovered()[i]) {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}
				} else {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (clauseInfo.getNegExamplesEvaluated()[i]) {
				if (clauseInfo.getNegExamplesCovered()[i]) {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}
					
				} else {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}
		
		// Update cached clauses
		cachedClauses.put(clauseString, examplesInfo);
		
		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

	/*
	* computeBatchScore method is introduced for CastorLearnerBatchGeneralization flow.
	* it uses following score computation formula:
	* clause score = previous score of clause + current score
	*/
	public double computeBatchScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();

		// Get string representation
		String clauseString = clauseToString(clauseInfo.getClause());

		// Get examples info if existing, if not create new
		ExamplesInfo examplesInfo;
		if (!cachedClauses.containsKey(clauseString)) {
			examplesInfo = new ExamplesInfo();
		}
		else {
			examplesInfo = cachedClauses.get(clauseString);
		}

		// Update evaluated and covered examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getPosExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getPosExamplesEvaluated()[i] = true;

					// Update covered
					if (examplesInfo.getPosExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getPosExamplesCovered()[i] = true;
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getNegExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getNegExamplesEvaluated()[i] = true;

					// Update covered
					if (examplesInfo.getNegExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getNegExamplesCovered()[i] = true;
					}
				}
			}
		}

		// Get new positive examples covered
		int newPosTotal = samplePosExamples.size();
		int newPosCoveredCount = coverageEngine.countCoveredExamplesFromBatchList(genericDAO, schema, clauseInfo, samplePosExamples, posExamplesRelation, true);

		// Get total negative examples covered
		int totalNeg = coverageEngine.getAllNegExamples().size();
		int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);

		// Compute statistics
		int truePositive = newPosCoveredCount;
		int falsePositive = negCoveredCount;
		int trueNegative = totalNeg - negCoveredCount;
		int falseNegative = newPosTotal - newPosCoveredCount;

		// Compute score
		double score = EvaluationFunctions.score(evaluationFunction, truePositive, falsePositive, trueNegative, falseNegative);
		clauseInfo.setScore(score);

		// Store information about covered and evaluated examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (clauseInfo.getPosExamplesEvaluated()[i]) {
				if (clauseInfo.getPosExamplesCovered()[i]) {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}
				} else {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (clauseInfo.getNegExamplesEvaluated()[i]) {
				if (clauseInfo.getNegExamplesCovered()[i]) {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}

				} else {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}

		// Update cached clauses
		cachedClauses.put(clauseString, examplesInfo);

		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

	/*
	* computeRandomSampleScore method is introduced for CastorLearnerRandomSampleGeneralization flow.
	* It uses the sample of both positive and negative example to compute the score
	*/
	public double computeRandomSampleScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, List<Tuple> sampleNegExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();

		// Get string representation
		String clauseString = clauseToString(clauseInfo.getClause());

		// Get examples info if existing, if not create new
		ExamplesInfo examplesInfo;
		if (!cachedClauses.containsKey(clauseString)) {
			examplesInfo = new ExamplesInfo();
		}
		else {
			examplesInfo = cachedClauses.get(clauseString);
		}

		// Update evaluated and covered examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getPosExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getPosExamplesEvaluated()[i] = true;

					// Update covered
					if (examplesInfo.getPosExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getPosExamplesCovered()[i] = true;
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
				if (examplesInfo.getNegExamplesInfo().get(exampleString).isEvaluated()) {
					// Update evaluated
					clauseInfo.getNegExamplesEvaluated()[i] = true;

					// Update covered
					if (examplesInfo.getNegExamplesInfo().get(exampleString).isCovered()) {
						clauseInfo.getNegExamplesCovered()[i] = true;
					}
				}
			}
		}

		// Get new positive examples covered
		int newPosTotal = samplePosExamples.size();
		int newPosCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, samplePosExamples, posExamplesRelation, true);

		// Get total negative examples covered
		int totalNeg = sampleNegExamples.size();
		int negCoveredCount = coverageEngine.countCoveredExamplesFromList(genericDAO, schema, clauseInfo, sampleNegExamples, negExamplesRelation, false);

		// Compute statistics
		int truePositive = newPosCoveredCount;
		int falsePositive = negCoveredCount;
		int trueNegative = totalNeg - negCoveredCount;
		int falseNegative = newPosTotal - newPosCoveredCount;

		// Compute score
		double score = EvaluationFunctions.score(evaluationFunction, truePositive, falsePositive, trueNegative, falseNegative);
		clauseInfo.setScore(score);

		// Store information about covered and evaluated examples
		// Positive examples
		for (int i = 0; i < coverageEngine.getAllPosExamples().size(); i++) {
			String exampleString = coverageEngine.getAllPosExamples().get(i).toString();
			if (clauseInfo.getPosExamplesEvaluated()[i]) {
				if (clauseInfo.getPosExamplesCovered()[i]) {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}
				} else {
					if (examplesInfo.getPosExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getPosExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getPosExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}
		// Negative examples
		for (int i = 0; i < coverageEngine.getAllNegExamples().size(); i++) {
			String exampleString = coverageEngine.getAllNegExamples().get(i).toString();
			if (clauseInfo.getNegExamplesEvaluated()[i]) {
				if (clauseInfo.getNegExamplesCovered()[i]) {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(true);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, true));
					}

				} else {
					if (examplesInfo.getNegExamplesInfo().containsKey(exampleString)) {
						examplesInfo.getNegExamplesInfo().get(exampleString).setEvaluated(true);
						examplesInfo.getNegExamplesInfo().get(exampleString).setCovered(false);
					} else {
						examplesInfo.getNegExamplesInfo().put(exampleString, new ExampleInfo(true, false));
					}
				}
			}
		}

		// Update cached clauses
		cachedClauses.put(clauseString, examplesInfo);

		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

	public boolean entails(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation) {
		TimeWatch tw = TimeWatch.start();
		
		boolean entails = false;
		String clauseString = clauseToString(clauseInfo.getClause());
		String exampleString = exampleTuple.toString();
		
		if (this.cachedClauses.containsKey(clauseString)) {
			if (this.cachedClauses.get(clauseString).getPosExamplesInfo().containsKey(exampleString)) {
				// Check information about example
				ExampleInfo exampleInfo = this.cachedClauses.get(clauseString).getPosExamplesInfo().get(exampleString);
				if (exampleInfo.isEvaluated() && exampleInfo.isCovered()) {
					entails = true;
				} else if (exampleInfo.isEvaluated() && !exampleInfo.isCovered()) {
					entails = false;
				} else {
					entails = coverageEngine.entails(genericDAO, schema, clauseInfo, exampleTuple, posExamplesRelation, true);
					exampleInfo.setEvaluated(true);
					exampleInfo.setCovered(entails);
				}
			} else {
				// Store new information about example
				entails = coverageEngine.entails(genericDAO, schema, clauseInfo, exampleTuple, posExamplesRelation, true);
				this.cachedClauses.get(clauseString).getPosExamplesInfo().put(exampleString, new ExampleInfo(true, entails));
			}
		} else {
			entails = coverageEngine.entails(genericDAO, schema, clauseInfo, exampleTuple, posExamplesRelation, true);
			
			// Store new information about example
			ExamplesInfo examplesInfo = new ExamplesInfo();
			examplesInfo.getPosExamplesInfo().put(exampleString, new ExampleInfo(true, entails));
			cachedClauses.put(clauseString, examplesInfo);
		}
		
		NumbersKeeper.entailmentTime += tw.time();
		return entails;
	}
	
	private String clauseToString(MyClause clause) {
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}
}
