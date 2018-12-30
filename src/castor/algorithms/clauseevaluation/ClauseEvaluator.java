package castor.algorithms.clauseevaluation;

import java.util.List;

import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;

public interface ClauseEvaluator {

	double computeScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction);
	double computeBatchScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction);
	double computeRandomSampleScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, List<Tuple> samplePosExamples, List<Tuple> sampleNegExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseInfo clauseInfo, EvaluationFunctions.FUNCTION evaluationFunction);
	boolean entails(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, ClauseInfo clauseInfo, Tuple exampleTuple, Relation posExamplesRelation);
}
