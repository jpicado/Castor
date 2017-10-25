package castor.algorithms;

import java.util.List;

import castor.algorithms.coverageengines.CoverageEngine;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.wrappers.EvaluationResult;

public interface Learner {

	List<ClauseInfo> learn(Schema schema, DataModel dataModel, Relation posExamplesRelation, Relation negExamplesRelation, String spNameTemplate, boolean globalDefinition);
	EvaluationResult evaluate(CoverageEngine testCoverageEngine, Schema schema, List<ClauseInfo> definition, Relation testExamplesPos, Relation testExamplesNeg);
	Parameters getParameters();
}
