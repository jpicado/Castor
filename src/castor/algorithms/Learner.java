package castor.algorithms;

import java.util.List;

import castor.algorithms.coverageengines.CoverageEngine;
import castor.hypotheses.ClauseInfo;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.wrappers.EvaluationResult;

public interface Learner {

	List<ClauseInfo> learn(Schema schema, Mode modeH, List<Mode> modesB, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate);
	EvaluationResult evaluate(CoverageEngine testCoverageEngine, Schema schema, List<ClauseInfo> definition, Relation testExamplesPos, Relation testExamplesNeg);
}
