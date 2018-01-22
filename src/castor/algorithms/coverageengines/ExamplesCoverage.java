package castor.algorithms.coverageengines;

import java.util.List;

import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.db.QueryGenerator;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.TimeWatch;

public class ExamplesCoverage {

	public static int entailmentCalls = 0;
	public static long entailmentTime = 0;
	
	/*
	 * Returns true if the clause covers the given example; false otherwise
	 */
	public static boolean clauseCoversExample(GenericDAO genericDAO, Schema schema, MyClause clause, Tuple exampleTuple, Relation relation) {
		TimeWatch tw = TimeWatch.start();
		boolean entails = false;
		String query = QueryGenerator.generateQueryClauseEntailsExample(schema, clause, exampleTuple, relation);
		GenericTableObject result = genericDAO.executeQuery(query);
		if (result != null && result.getTable().size() > 0) {
			entails = true;
		}
		
		ExamplesCoverage.entailmentTime += tw.time();
		ExamplesCoverage.entailmentCalls++;
		return entails;
	}
	
	/*
	 * Returns the number of tuples in coverageRelation covered by the clause
	 */
	public static int clauseCoverage(GenericDAO genericDAO, Schema schema, MyClause clause, Relation coverageRelation) {
		// Create query to compute coverage of relation		
		String query = QueryGenerator.generateQueryFromClauseAndCoverageTable(schema, clause, coverageRelation, true);
		long score = genericDAO.executeScalarQuery(query);
		return (int)score;
	}
	
	/*
	 * Returns the number of positive examples covered by the clause
	 */
	public static int clauseCoveragePosExamples(GenericDAO genericDAO, Schema schema, MyClause clause, List<Tuple> remainingPosExamples, Relation coverageRelation) {
		int score = 0;
		String posCoverageQuery = QueryGenerator.generateQueryFromClauseAndCoverageTable(schema, clause, coverageRelation, false);
		GenericTableObject result = genericDAO.executeQuery(posCoverageQuery);
		List<Tuple> coveredPosExamples = result.getTable();
		coveredPosExamples.retainAll(remainingPosExamples);
		score = coveredPosExamples.size();
		return score;
	}
	
	/*
	 * Returns the number of tuples in coverageRelation covered by the definition 
	 */
	public static int definitionCoverage(GenericDAO genericDAO, Schema schema, List<MyClause> definition, Relation coverageRelation) {
		// Create query to compute total coverage of definition
		String query = QueryGenerator.generateQueryFromDefinitionAndCoverageTable(schema, definition, coverageRelation);
		long score = genericDAO.executeScalarQuery(query);
		return (int)score;
	}
}
