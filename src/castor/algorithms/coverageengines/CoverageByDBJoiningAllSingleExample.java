package castor.algorithms.coverageengines;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.db.QueryGenerator;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;

public class CoverageByDBJoiningAllSingleExample implements CoverageEngine {

	private List<Tuple> allPosExamples;
	private List<Tuple> allNegExamples;
	
	public CoverageByDBJoiningAllSingleExample(GenericDAO genericDAO, Relation posExamplesRelation, Relation negExamplesRelation) {
		this.initialize(genericDAO, posExamplesRelation, negExamplesRelation);
	}
	
	private void initialize(GenericDAO genericDAO, Relation posExamplesRelation, Relation negExamplesRelation) {
		// Get all positive and negative examples
		String posCoverageQuery = QueryGenerator.generateQuerySelectAllTuples(posExamplesRelation, true);
		GenericTableObject posResult = genericDAO.executeQuery(posCoverageQuery);
		this.allPosExamples = posResult.getTable();
		
		String negCoverageQuery = QueryGenerator.generateQuerySelectAllTuples(negExamplesRelation, true);
		GenericTableObject negResult = genericDAO.executeQuery(negCoverageQuery);
		this.allNegExamples = negResult.getTable();
	}
	
	@Override
	public List<Tuple> getAllPosExamples() {
		return allPosExamples;
	}

	@Override
	public List<Tuple> getAllNegExamples() {
		return allNegExamples;
	}
	
	@Override
	public boolean entails(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Tuple example, Relation examplesRelation, boolean positiveRelation) {
		return ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation);
	}
	
	@Override
	public int countCoveredExamplesFromList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean positiveRelation) {
		int covered = 0;
		
		// Compute positive coverage
		for (Tuple example : examples) {
			if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
				covered++;
			}
		}

		return covered;
	}
	
	@Override
	public int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean positiveRelation) {
		int covered = 0;
		
		// Get all examples from relation
		String query = QueryGenerator.generateQuerySelectAllTuples(examplesRelation, false);
		GenericTableObject result = genericDAO.executeQuery(query);
		List<Tuple> examples = result.getTable();
		// Compute coverage
		for (Tuple example : examples) {
			if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
				covered++;
			}
		}
		
		return covered;
	}

	@Override
	public int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean positiveRelation) {
		List<MyClause> clauses = new LinkedList<MyClause>();
		for (ClauseInfo clauseInfo : definition) {
			clauses.add(clauseInfo.getClause());
		}
		return ExamplesCoverage.definitionCoverage(genericDAO, schema, clauses, examplesRelation);
	}
	
	@Override
	public boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation) {
		boolean[] coveredExamples = null;
		
		// Get all examples from relation
		String query = QueryGenerator.generateQuerySelectAllTuples(examplesRelation, false);
		GenericTableObject result = genericDAO.executeQuery(query);
		List<Tuple> examples = result.getTable();
		coveredExamples = new boolean[examples.size()];
		// Compute coverage
		for (int i = 0; i < examples.size(); i++) {
			Tuple example = examples.get(i);
			if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
				coveredExamples[i] = true;
			}
		}
		
		return coveredExamples;
	}
	
	@Override
	public boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean isPositiveRelation) {
		boolean[] coveredExamples = null;
		
		// Get all examples from relation
		String query = QueryGenerator.generateQuerySelectAllTuples(examplesRelation, false);
		GenericTableObject result = genericDAO.executeQuery(query);
		List<Tuple> examples = result.getTable();
		coveredExamples = new boolean[examples.size()];
		
		// Compute coverage for each clause
		for (ClauseInfo clauseInfo : definition) {
			for (int i = 0; i < examples.size(); i++) {
				Tuple example = examples.get(i);
				if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
					coveredExamples[i] = true;
				}
			}
		}
		
		return coveredExamples;
	}
	
	@Override
	public List<Tuple> coveredExamplesTuplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation) {
		List<Tuple> coveredExamples = new LinkedList<Tuple>();
		
		List<Tuple> allExamples;
		if (isPositiveRelation) {
			allExamples = this.allPosExamples;
		} else {
			allExamples = this.allNegExamples;
		}
		
		for (Tuple example : allExamples) {
			if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
				coveredExamples.add(example);
			}
		}
		return coveredExamples;
	}
	
	@Override
	public List<Tuple> coveredExamplesTuplesFromList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean positiveRelation) {
		List<Tuple> coveredExamplesTuples = new ArrayList<Tuple>();
		
		// Compute positive coverage
		for (Tuple example : examples) {
			if (ExamplesCoverage.clauseCoversExample(genericDAO, schema, clauseInfo.getClause(), example, examplesRelation)) {
				coveredExamplesTuples.add(example);
			}
		}

		return coveredExamplesTuples;
	}
}
