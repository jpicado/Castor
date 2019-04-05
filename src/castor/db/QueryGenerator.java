package castor.db;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.datastructure.Pair;
import castor.algorithms.transformations.ClauseTransformations;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.Commons;

import java.util.*;
import java.util.Map.Entry;

public class QueryGenerator {

	public static String generateQuerySelectAllTuples(Relation relation, boolean distinct) {
		String query;
		if (distinct) {
			query = "SELECT DISTINCT * FROM " + relation.getName() + ";";
		} else {
			query = "SELECT * FROM " + relation.getName() + ";";
		}
		return query;
	}

	public static String generateQueryCountTuples(Relation relation, boolean distinct) {
		String query;
		if (distinct) {
			query = "SELECT COUNT(DISTINCT *) FROM " + relation.getName() + ";";
		} else {
			query = "SELECT COUNT(*) FROM " + relation.getName() + ";";
		}
		return query;
	}

	public static String generateQueryDropTable(Relation relation) {
		String query = "DROP TABLE " + relation.getName() + ";";
		return query;
	}

	public static List<String> generateCoverageQueries2(Schema schema, MyClause clause, Relation finalRelation) throws Exception {
		List<String> queries = new LinkedList<String>();

		if (!clause.isDefiniteClause()) {
			throw new Exception("Only definite clauses are allowed.");
		}
		if (clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().size() != finalRelation.getAttributeNames().size()) {
			throw new Exception("Arity of head of clause does not match arity of final relation.");
		}

		// Keep track of the attribute corresponding to the first appearance of each variable
		Map<String, String> termAppearance = new HashMap<String, String>();

		if (clause.getNumberNegativeLiterals() > 0) {
			List<Literal> bodyLiterals = clause.getNegativeLiterals();

			String tempTableName = "TEMP";
			//TODO Hardcoded data type
			String dataType = "VARCHAR(32)";
			String createQuery, insertQuery, whereCondition, dropQuery, currentTempTable;
			int tableCounter = 0;
			int currentTempTableArity = 0;

			// Create SQL statements to create FIRST temporary table and insert tuples
			currentTempTable = tempTableName + tableCounter;
			tableCounter++;

			Literal firstLiteral = bodyLiterals.get(0);
			String predicate = firstLiteral.getAtomicSentence().getSymbolicName();
			Relation relation = schema.getRelations().get(predicate);

			createQuery = "CREATE TABLE " + currentTempTable + "(";
			insertQuery = "INSERT INTO " + currentTempTable + " SELECT ";

			for (int i = 0; i < firstLiteral.getAtomicSentence().getArgs().size(); i++) {
				// Create
				String attributeName = "att" + i;
				createQuery += attributeName + " " + dataType;
				currentTempTableArity++;
				// Insert
				insertQuery += predicate + "." + relation.getAttributeNames().get(i);

				if (i < firstLiteral.getAtomicSentence().getArgs().size() - 1) {
					createQuery += ", ";
					insertQuery += ", ";
				}

				// Store attribute where variable first appeared
				String termName = firstLiteral.getAtomicSentence().getArgs().get(i).getSymbolicName();
				if (!termAppearance.containsKey(termName)) {
					termAppearance.put(termName, attributeName);
				}
			}
			createQuery += ");";
			insertQuery += " FROM " + predicate + ";";

			queries.add(createQuery);
			queries.add(insertQuery);

			// For each body literal (starting from second literal), do:
			// 1. Create new temporary table with # of columns = # of columns of previous temporary table + # of columns of literal
			// 2. Insert into new temporary table: join between temporary table and literal, projecting on all columns of previous temporary table + columns of current literal
			// 3. Delete old temporary table

			for (int j = 1; j < bodyLiterals.size(); j++) {
				Literal literal = bodyLiterals.get(j);
				predicate = literal.getAtomicSentence().getSymbolicName();
				relation = schema.getRelations().get(predicate);

				String oldTempTable = currentTempTable;
				currentTempTable = tempTableName + tableCounter;
				tableCounter++;

				int oldTempTableArity = currentTempTableArity;
				currentTempTableArity = 0;
				int attributeCounter = 0;

				createQuery = "CREATE TABLE " + currentTempTable + "(";
				insertQuery = "INSERT INTO " + currentTempTable + " SELECT " + oldTempTable + ".*";
				whereCondition = "";

				// Add attributes for create query from old temporary table
				for (int i = 0; i < oldTempTableArity; i++) {
					createQuery += "att" + attributeCounter + " " + dataType;
					if (i < oldTempTableArity - 1) {
						createQuery += ", ";
					}
					attributeCounter++;
					currentTempTableArity++;
				}

				// Add attribute for create query from new joined table
				// Update insert query and where condition
				boolean first = true;
				for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
					String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();

					// If term has not been seen:
					// -store attribute from where it is seen
					// -add it to query projection
					if (!termAppearance.containsKey(termName)) {
						// Insert
						insertQuery += ", " + predicate + "." + relation.getAttributeNames().get(i);

						// Create
						String attributeName = "att" + attributeCounter;
						createQuery += ", " + attributeName + " " + dataType;
						attributeCounter++;
						currentTempTableArity++;

						termAppearance.put(termName, attributeName);
					} else {
						// Where condition
						if (!first) {
							whereCondition += "AND ";
						} else {
							first = false;
						}
						whereCondition += oldTempTable + "." + termAppearance.get(termName)
								+ " = "
								+ predicate + "." + schema.getRelations().get(predicate).getAttributeNames().get(i) + " ";
					}
				}
				createQuery += ");";
				insertQuery += " FROM " + oldTempTable + ", " + predicate;
				if (!whereCondition.isEmpty()) {
					insertQuery += " WHERE " + whereCondition;
				}
				insertQuery += ";";

				// Drop query
				dropQuery = "DROP TABLE " + oldTempTable + ";";

				queries.add(createQuery);
				queries.add(insertQuery);
				queries.add(dropQuery);
			}

			// Create table with only attributes given by head of clause
			Literal headLiteral = clause.getPositiveLiterals().get(0);
			createQuery = "CREATE TABLE " + finalRelation.getName() + "(";
			insertQuery = "INSERT INTO " + finalRelation.getName() + " SELECT DISTINCT ";
			for (int i = 0; i < finalRelation.getAttributeNames().size(); i++) {
				// Create
				createQuery += finalRelation.getAttributeNames().get(i) + " " + dataType;

				String termName = headLiteral.getAtomicSentence().getArgs().get(i).getSymbolicName();
				// Insert
				if (termAppearance.containsKey(termName)) {
					insertQuery += termAppearance.get(termName);
				} else {
					// In case this head variable did not appear in body
					insertQuery += "'' as att" + i;
				}

				if (i < finalRelation.getAttributeNames().size() - 1) {
					createQuery += ", ";
					insertQuery += ", ";
				}
			}
			createQuery += ");";
			insertQuery += " FROM " + currentTempTable + ";";
			dropQuery = "DROP TABLE " + currentTempTable + ";";

			queries.add(createQuery);
			queries.add(insertQuery);
			queries.add(dropQuery);
		}

		return queries;
	}

	public static String generateQueryCountJoinTables(Relation relation1, Relation relation2) throws Exception {
		if (relation1.getAttributeNames().size() != relation2.getAttributeNames().size()) {
			throw new Exception("Recevied relations with different number of attributes.");
		}

		String subquery = "SELECT ";
		String whereCondition = "";
		boolean first = true;
		for (int i = 0; i < relation1.getAttributeNames().size(); i++) {
			// Select query
			subquery += relation1.getName() + "." + relation1.getAttributeNames().get(i);
			if (i < relation1.getAttributeNames().size() - 1) {
				subquery += ", ";
			}

			// Where condition
			if (first) {
				first = false;
			} else {
				whereCondition += "AND ";
			}
			whereCondition += relation1.getName() + "." + relation1.getAttributeNames().get(i) + "=" + relation2.getName() + "." + relation2.getAttributeNames().get(i) + " ";
		}
		subquery += " FROM " + relation1.getName() + ", " + relation2.getName() + " WHERE " + whereCondition;

		String query = "SELECT COUNT(*) FROM (" + subquery + ") t;";
		return query;
	}


	/*
     * Given clause C and example e, generate query to check if C entails e w.r.t. DB
     */
	public static String generateQueryClauseEntailsExample(Schema schema, MyClause clause, Tuple tuple, Relation posExamplesRelation) {
		StringBuilder query = new StringBuilder();
		clause = ClauseTransformations.reorderClauseForHeadConnected(clause);

		int tableCounter = 0;
		//String headPredicate = clause.getPositiveLiterals().get(0).getAtomicSentence().getSymbolicName();
		String headPredicate = posExamplesRelation.getName();
		String headPredicateAlias = Commons.newAlias(tableCounter);
		tableCounter++;

		// Keep track of the relation and attribute corresponding to the first appearance of each variable
		Map<String, Pair<String, String>> termAppearance = new HashMap<String, Pair<String, String>>();
		// Get attribute names for variables in head predicate
		for (int i = 0; i < clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().size(); i++) {
			String termName = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().get(i).getSymbolicName();
			if (!termAppearance.containsKey(termName)) {
				//termAppearance.put(termName, new Pair<String,String>(headPredicateAlias, schema.getRelations().get(headPredicate).getAttributeNames().get(i)));
				termAppearance.put(termName, new Pair<String, String>(headPredicateAlias, tuple.getValues().get(i).toString()));
			}
		}

		// Build query
		query.append("SELECT DISTINCT ");

		// Add column names to select
		for (int i = 0; i < schema.getRelations().get(headPredicate).getAttributeNames().size(); i++) {
			String attribute = schema.getRelations().get(headPredicate).getAttributeNames().get(i);
			query.append(headPredicateAlias + "." + attribute + " as att" + (i + 1) + " ");
			if (i < schema.getRelations().get(headPredicate).getAttributeNames().size() - 1) {
				query.append(", ");
			}
		}
		query.append("FROM ");

		// Create subquery to select single tuple from head predicate relation
		query.append("(SELECT * FROM " + headPredicate + " WHERE ");
		boolean firstInHead = true;
		for (int i = 0; i < schema.getRelations().get(headPredicate).getAttributeNames().size(); i++) {
			if (!firstInHead) {
				query.append("AND ");
			} else {
				firstInHead = false;
			}

			String attribute = schema.getRelations().get(headPredicate).getAttributeNames().get(i);
			String value = tuple.getValues().get(i).toString();
			query.append(attribute + " = '" + value + "' ");
		}
		query.append(") " + headPredicateAlias + " ");

		// Add join predicates
		for (Literal literal : clause.getNegativeLiterals()) {
			String predicateName = literal.getAtomicSentence().getSymbolicName();
			String predicateAlias = Commons.newAlias(tableCounter);
			tableCounter++;
			query.append("JOIN " + predicateName + " as " + predicateAlias + " ON ");

			// Create join condition
			boolean first = true;
			for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
				String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();
				if (termAppearance.containsKey(termName)) {
					if (!first) {
						query.append("AND ");
					} else {
						first = false;
					}

					String valueToCompare;
					if (termAppearance.get(termName).getFirst().equals(headPredicateAlias)) {
						valueToCompare = "'" + termAppearance.get(termName).getSecond() + "'";
					} else {
						valueToCompare = termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond();
					}
					query.append(predicateAlias + "." + schema.getRelations().get(predicateName.toUpperCase()).getAttributeNames().get(i)
							+ " = "
							+ valueToCompare + " ");

//					query.append(termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond()
//							+ " = "
//							+ predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i) + " ");

				} else {
					termAppearance.put(termName, new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName.toUpperCase()).getAttributeNames().get(i)));
				}
			}
		}

		return query.toString();
	}

	public static String generateQueryClauseEntailsExample2(Schema schema, MyClause clause, Tuple tuple) {
		StringBuilder query = new StringBuilder();

		int tableCounter = 0;
		String headPredicate = clause.getPositiveLiterals().get(0).getAtomicSentence().getSymbolicName();
		String headPredicateAlias = Commons.newAlias(tableCounter);
		tableCounter++;

		// Keep track of the relation and attribute corresponding to the first appearance of each variable
		Map<String, Pair<String, String>> termAppearance = new HashMap<String, Pair<String, String>>();
		// Get attribute names for variables in head predicate
		for (int i = 0; i < clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().size(); i++) {
			String termName = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().get(i).getSymbolicName();
			if (!termAppearance.containsKey(termName)) {
				//termAppearance.put(termName, new Pair<String,String>(headPredicateAlias, schema.getRelations().get(headPredicate).getAttributeNames().get(i)));
				termAppearance.put(termName, new Pair<String, String>(headPredicateAlias, tuple.getValues().get(i).toString()));
			}
		}

		// Build query
		query.append("SELECT ");

		// Add column names to select
		for (int i = 0; i < schema.getRelations().get(headPredicate).getAttributeNames().size(); i++) {
			String attribute = schema.getRelations().get(headPredicate).getAttributeNames().get(i);
			query.append(headPredicateAlias + "." + attribute + " as att" + (i + 1) + " ");
			if (i < schema.getRelations().get(headPredicate).getAttributeNames().size() - 1) {
				query.append(", ");
			}
		}
		query.append("FROM ");

		// Create subquery to select single tuple from head predicate relation
		//query.append(headPredicate + " as " + headPredicateAlias + " ");
		query.append("(SELECT * FROM " + headPredicate + " WHERE ");
		boolean firstInHead = true;
		for (int i = 0; i < schema.getRelations().get(headPredicate).getAttributeNames().size(); i++) {
			if (!firstInHead) {
				query.append("AND ");
			} else {
				firstInHead = false;
			}

			String attribute = schema.getRelations().get(headPredicate).getAttributeNames().get(i);
			String value = tuple.getValues().get(i).toString();
			query.append(attribute + " = '" + value + "' ");
		}
		query.append(") " + headPredicateAlias + " ");

		// Add join predicates
		for (Literal literal : clause.getNegativeLiterals()) {
			String predicateName = literal.getAtomicSentence().getSymbolicName();
			String predicateAlias = Commons.newAlias(tableCounter);
			tableCounter++;
			query.append("JOIN " + predicateName + " as " + predicateAlias + " ON ");

			// Create join condition
			boolean first = true;
			for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
				String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();
				if (termAppearance.containsKey(termName)) {
					if (!first) {
						query.append("AND ");
					} else {
						first = false;
					}

					String valueToCompare;
					if (termAppearance.get(termName).getFirst().equals(headPredicateAlias)) {
						valueToCompare = "'" + termAppearance.get(termName).getSecond() + "'";
					} else {
						valueToCompare = termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond();
					}
					query.append(predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i)
							+ " = "
							+ valueToCompare + " ");

//					query.append(termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond()
//							+ " = "
//							+ predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i) + " ");
//
				} else {
					termAppearance.put(termName, new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
				}
			}
		}

		return query.toString();
	}

	/*
     * Given a definition H and table T, generate query to compute the count of tuples covered in T by all clauses in H
     */
	public static String generateQueryFromDefinitionAndCoverageTable(Schema schema, List<MyClause> definition, Relation tableToCover) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) FROM (");
		for (int i = 0; i < definition.size(); i++) {
			MyClause clause = definition.get(i);
			query.append(QueryGenerator.generateQueryFromClauseAndCoverageTable(schema, clause, tableToCover, false));
			if (i < definition.size() - 1) {
				query.append(" UNION ");
			}
		}
		query.append(") t0;");

		return query.toString();
	}

	/*
     * Given a clause C and table T, generate query to compute tuples in T covered by C
     * If countTuples is true, return query to compute count, instead of all tuples
     */
	public static String generateQueryFromClauseAndCoverageTable(Schema schema, MyClause clause, Relation tableToCover, boolean countTuples) {
		StringBuilder query = new StringBuilder();

		clause = ClauseTransformations.reorderClauseForHeadConnected(clause);

		int tableCounter = 0;
		String headPredicate = tableToCover.getName();
		String headPredicateAlias = Commons.newAlias(tableCounter);
		tableCounter++;

		// Keep track of the relation and attribute corresponding to the first appearance of each variable
		Map<String, Pair<String, String>> termAppearance = new HashMap<String, Pair<String, String>>();
		// Get attribute names for variables in head predicate
		for (int i = 0; i < clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().size(); i++) {
			String termName = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs().get(i).getSymbolicName();
			if (!termAppearance.containsKey(termName)) {
				termAppearance.put(termName, new Pair<String, String>(headPredicateAlias, tableToCover.getAttributeNames().get(i)));
			}
		}

		if (countTuples) {
			// Build query to obtain count
			query.append("SELECT COUNT(*) FROM (");
		}

		// Build subquery
		query.append("SELECT DISTINCT ");

		// Add column names to select
		for (int i = 0; i < tableToCover.getAttributeNames().size(); i++) {
			String attribute = tableToCover.getAttributeNames().get(i);
			query.append(headPredicateAlias + "." + attribute + " ");
			if (i < tableToCover.getAttributeNames().size() - 1) {
				query.append(", ");
			}
		}
		query.append("FROM ");
		query.append(headPredicate + " as " + headPredicateAlias + " ");

		// Add join predicates
		for (Literal literal : clause.getNegativeLiterals()) {
			String predicateName = literal.getAtomicSentence().getSymbolicName().toUpperCase();
			String predicateAlias = Commons.newAlias(tableCounter);
			tableCounter++;


			// Create join condition
			boolean first = true;
			String joinCondition = "";
			for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
				String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();
				if (termAppearance.containsKey(termName)) {
					if (!first) {
						joinCondition += "AND ";
					} else {
						first = false;
					}

					joinCondition += termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond()
							+ " = "
							+ predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i) + " ";

				} else {
					termAppearance.put(termName, new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
				}
			}
			if (!joinCondition.isEmpty()) {
				query.append("JOIN " + predicateName + " as " + predicateAlias + " ON " + joinCondition);
			}
		}

		if (countTuples) {
			query.append(") " + Commons.newAlias(0));
		}
//		query.append(";");

		return query.toString();
	}

	public static String generateQueryFromClause(Schema schema, ClauseInfo clauseInfo) {
		MyClause clause = ClauseTransformations.reorderClauseForHeadConnected(clauseInfo.getClause());

		int tableCounter = 0;

		// Keep track of the relation and attribute corresponding to the first appearance of each variable
		Map<String, Pair<String, String>> termAppearance = new HashMap<String, Pair<String, String>>();

		// Add select and join predicates
		StringBuilder queryJoin = new StringBuilder();
		queryJoin.append("FROM ");
		StringBuilder querySelect = new StringBuilder();

		//joinConditionsList list join conditions such as T0.ID = T2.ID
		//queryJoinMap stores predicates and alias such as T0 -> COT_EVENT_POSITION AS T0
		Map<String, String> queryJoinMap = new HashMap<>();
		List<String> joinConditionList = new ArrayList<>();

		boolean firstLiteral = true;
		boolean firstWhereCondition = true;
		for (Literal literal : clause.getNegativeLiterals()) {
			String predicateName = literal.getAtomicSentence().getSymbolicName().toUpperCase();
			String predicateAlias = Commons.newAlias(tableCounter);
			tableCounter++;

			if (firstLiteral) {
				for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
					String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();

					if (Commons.isVariable(termName)) {
						if (!termAppearance.containsKey(termName)) {
							termAppearance.put(termName, new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
						}
					} else {
						if (!firstWhereCondition) {
							querySelect.append(" AND ");
						} else {
							firstWhereCondition = false;
						}
						termName = termName.replace("\"", "'");
						termName = Commons.unescapeMetaCharacters(termName);
						querySelect.append(predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i)
								+ " = "
								+ termName);
					}

					// Check head substitutions stored in clauseInfo
					for (Entry<Variable, Term> entry : clauseInfo.getHeadSubstitutions().entrySet()) {
						String valueString = entry.getValue().getSymbolicName().replace("\"", "'");
						if (termName.equals(valueString)) {
							termAppearance.put(entry.getKey().getSymbolicName(), new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
						}
					}
				}
				
				firstLiteral = false;
			} else {
				for (int i = 0; i < literal.getAtomicSentence().getArgs().size(); i++) {
					String termName = literal.getAtomicSentence().getArgs().get(i).getSymbolicName();

					if (Commons.isVariable(termName)) {
						if (termAppearance.containsKey(termName)) {
							//Fill joinConditionMap
							joinConditionList.add(termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond()
									+ " = "
									+ predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i) + " ");
						} else {
							termAppearance.put(termName, new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
						}
					} else {
						if (!firstWhereCondition) {
							querySelect.append(" AND ");
						} else {
							firstWhereCondition = false;
						}
						termName = termName.replace("\"", "'");
						termName = Commons.unescapeMetaCharacters(termName);
						querySelect.append(predicateAlias + "." + schema.getRelations().get(predicateName).getAttributeNames().get(i)
								+ " = "
								+ termName);
					}

					// Check head substitutions stored in clauseInfo
					for (Entry<Variable, Term> entry : clauseInfo.getHeadSubstitutions().entrySet()) {
						String valueString = entry.getValue().getSymbolicName().replace("\"", "'");
						if (termName.equals(valueString)) {
							termAppearance.put(entry.getKey().getSymbolicName(), new Pair<String, String>(predicateAlias, schema.getRelations().get(predicateName).getAttributeNames().get(i)));
						}
					}
				}
			}
			
			//Fill queryJoinMap
			String predicateNameAndAlias = predicateName + " AS " + predicateAlias + " ";
			queryJoinMap.put(predicateAlias, predicateNameAndAlias);
			
			// If size is 1, simply add predicate to join list
			if (clause.getNumberNegativeLiterals() == 1) {
				joinConditionList.add(predicateNameAndAlias);
			}
		}
		
		if (clause.getNumberNegativeLiterals() == 1) {
			// If size is 1, simply add predicate to queryJoin
			queryJoin.append(joinConditionList.get(0));
		} else {
			//Bug fix for incorrect join order
			//visitedJoins stores all the visited joinTerms
			Set<String> visitedJoins = new HashSet<>();
			//Loop until all the Join conditions in joinConditionMap are not used. This is to handle special condition #1
			int lastSize = 0;
			while(!joinConditionList.isEmpty()) {
				Iterator<String> it = joinConditionList.iterator();
				while (it.hasNext()) {
					String entry = it.next();
					String entryStripped = entry.replaceAll("\\s+","");
					String [] joinCondition = entryStripped.split("=");
					String[] joinCondition1 = joinCondition[0].split("\\.");
					String[] joinCondition2 = joinCondition[1].split("\\.");
					String joinTerm1 = joinCondition1[0];
					String joinTerm2 = joinCondition2[0];
	
					if (visitedJoins.isEmpty()) {
						queryJoin.append(queryJoinMap.get(joinTerm1) + "JOIN " + queryJoinMap.get(joinTerm2) + "ON " + entry);
						visitedJoins.add(joinTerm1);
						visitedJoins.add(joinTerm2);
						it.remove();
					} else if (visitedJoins.contains(joinTerm1) && !visitedJoins.contains(joinTerm2)) {
						queryJoin.append("JOIN " + queryJoinMap.get(joinTerm2) + "ON " + entry);
						visitedJoins.add(joinTerm2);
						it.remove();
					} else if (!visitedJoins.contains(joinTerm1) && visitedJoins.contains(joinTerm2)) {
						queryJoin.append("JOIN " + queryJoinMap.get(joinTerm1) + "ON " + entry);
						visitedJoins.add(joinTerm1);
						it.remove();
					}else if(visitedJoins.contains(joinTerm1) && visitedJoins.contains(joinTerm2)){
						queryJoin.append("AND " + entry);
						it.remove();
					}
					//#1 - Special condition if both the terms are new in join i.e both are not in vistedJoinsList then simply keep it. In next iteration it will be processed.
					else if(!visitedJoins.contains(joinTerm1) && !visitedJoins.contains(joinTerm2)){
						continue;
					}
				}
				//This is to make sure that loop doesn't run infinitely
				if(joinConditionList.size()==lastSize){
					break;
				}
				lastSize = joinConditionList.size();
			}
		}

		// Add projections
		StringBuilder queryProject = new StringBuilder();
		queryProject.append("SELECT ");

		Literal headLiteral = clause.getPositiveLiterals().get(0);
		for (int i = 0; i < headLiteral.getAtomicSentence().getArgs().size(); i++) {
			String termName = headLiteral.getAtomicSentence().getArgs().get(i).getSymbolicName();
			if (termAppearance.containsKey(termName)) {
				String projection = termAppearance.get(termName).getFirst() + "." + termAppearance.get(termName).getSecond();
				queryProject.append(projection + " ");
			} else {
				queryProject.append("''" + " ");
			}
			if (i < headLiteral.getAtomicSentence().getArgs().size() - 1) {
				queryProject.append(", ");
			}
		}

		String fullQuery = queryProject.toString() + queryJoin.toString();
		if (querySelect.length() > 0) {
			fullQuery += "WHERE " + querySelect.toString();
		}
		fullQuery += ";";
		return fullQuery;
	}
}
