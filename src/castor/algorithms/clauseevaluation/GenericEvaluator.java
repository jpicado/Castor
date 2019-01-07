package castor.algorithms.clauseevaluation;

import java.util.List;

import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

public class GenericEvaluator implements ClauseEvaluator {

	public double computeScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();

		if (clauseInfo.getScore() == null) {
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
		}

		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

	public boolean entails(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation) {
		TimeWatch tw = TimeWatch.start();
		boolean entails = coverageEngine.entails(genericDAO, schema, clauseInfo, exampleTuple, posExamplesRelation, true);
		NumbersKeeper.entailmentTime += tw.time();
		return entails;
	}

	/*
	* computeBatchScore method is introduced for CastorLearnerBatchGeneralization flow.
	* it uses following score computation formula:
	* clause score = previous score of clause + current score
	*/
	public double computeBatchScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();

		if (clauseInfo.getScore() == null) {
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
		}

		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

	/*
	* computeRandomSampleScore method is introduced for CastorLearnerRandomSampleGeneralization flow.
	* It uses the sample of both positive and negative example to compute the score
	*/
	public double computeRandomSampleScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, List<Tuple> sampleNegExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction) {
		TimeWatch tw = TimeWatch.start();

		if (clauseInfo.getScore() == null) {
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
		}

		NumbersKeeper.scoringTime += tw.time();
		return clauseInfo.getScore();
	}

}