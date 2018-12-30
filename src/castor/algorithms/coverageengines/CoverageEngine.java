package castor.algorithms.coverageengines;

import java.util.List;

import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;

public interface CoverageEngine {
	
	List<Tuple> getAllPosExamples();
	List<Tuple> getAllNegExamples();
	
	boolean entails(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Tuple example, Relation examplesRelation, boolean positiveRelation);
	int countCoveredExamplesFromList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean positiveRelation);
	int countCoveredExamplesFromBatchList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean positiveRelation);
	int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean positiveRelation);
	int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean positiveRelation);
	boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation);
	boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean isPositiveRelation);
	List<Tuple> coveredExamplesTuplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation);
	List<Tuple> coveredExamplesTuplesFromList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean isPositiveRelation);
}
