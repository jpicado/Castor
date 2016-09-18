package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.db.dataaccess.GenericDAO;
import castor.db.dataaccess.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.Commons;

public class BottomClauseGeneratorOriginalAlgorithm {

	private static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s;";
	
	/*
	 * Generate bottom clause for one example
	 */
	public MyClause generateBottomClause(GenericDAO genericDAO, Tuple exampleTuple, Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall) {
		Map<String,String> hash = new HashMap<String,String>();
		return this.generateBottomClause(genericDAO, hash, exampleTuple, schema, modeH, modesB, iterations, recall);
	}
	
	/*
	 * Generate bottom clause for each input example in examples list
	 * Reuses hash function to keep consistency between variable associations
	 */
	public List<MyClause> generateBottomClauses(GenericDAO genericDAO, List<Tuple> examples, Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall) {
		List<MyClause> bottomClauses = new LinkedList<MyClause>();
		Map<String,String> hash = new HashMap<String,String>();
		for (Tuple example : examples) {
			bottomClauses.add(this.generateBottomClause(genericDAO, hash, example, schema, modeH, modesB, iterations, recall));
		}
		return bottomClauses;
	}
	
	/*
	 * Bottom clause generation as described in original algorithm
	 * Assumes that exampleTuple is for relation with same name as modeH
	 */
	public MyClause generateBottomClause(GenericDAO genericDAO, Map<String,String> hash, Tuple exampleTuple, Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall) {
		MyClause clause = new MyClause();
		Map<String,Set<String>> inTerms = new HashMap<String, Set<String>>();
		
		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		// Create head literal
		List<Term> headTerms = new ArrayList<Term>();
		int varCounter = 0;
		for (int i = 0; i < modeH.getArguments().size(); i++) {
			String value = exampleTuple.getValues().get(i);
			if (modeH.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				headTerms.add(new Constant(value));
			} else {
				// INPUT or OUTPUT type
				if(!hash.containsKey(value)) {
					hash.put(value, Commons.newVariable(varCounter));
					varCounter++;
				}
				headTerms.add(new Variable(hash.get(value)));
			}
			// Add constants to inTerms
			if (modeH.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
				String variableType = modeH.getArguments().get(i).getType();
				if(!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).add(value);
			}
		}
		Predicate headLiteral = new Predicate(modeH.getPredicateName(), headTerms);
		clause.addPositiveLiteral(headLiteral);
		
		// Create body literals
		for (int j = 0; j < iterations; j++) {

			// Create new inTerms to hold terms for this iteration
			Map<String,Set<String>> newInTerms = new HashMap<String, Set<String>>();

			for (Mode mode : modesB) {
				// Find position of input variable
				//TODO extend to handle multiple input variables
				int inputVarPosition = 0;
				for (int i = 0; i < mode.getArguments().size(); i++) {
					if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
						inputVarPosition = i;
						break;
					}
				}
				
				String attributeName = schema.getRelations().get(mode.getPredicateName()).getAttributeNames().get(inputVarPosition);
				String attributeType = mode.getArguments().get(inputVarPosition).getType();
				
				// If there is no list of known terms for attributeType, skip mode
				if (!inTerms.containsKey(attributeType)) {
					continue;
				}
				String knownTerms = toListString(inTerms.get(attributeType));
				
				// Create query and run
				String query = String.format(SELECTIN_SQL_STATEMENT, mode.getPredicateName(), attributeName, knownTerms);
	            GenericTableObject result = genericDAO.executeQuery(query);
	            
	            if (result != null) {
	        		int solutionsCounter = 0;
	            	for (Tuple row : result.getTable()) {
	            		
	            		if (solutionsCounter >= recall)
	            			break;
	            		
	            		List<Term> terms = new ArrayList<Term>();
		            	for (int i = 0; i < mode.getArguments().size(); i++) {
		            		String value = row.getValues().get(i);
		            		
		            		if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
		            			terms.add(new Constant(value));
		        			} else {
		        				// INPUT or OUTPUT type
		        				if(!hash.containsKey(value)) {
		        					hash.put(value, Commons.newVariable(varCounter));
		        					varCounter++;
		        				}
		        				terms.add(new Variable(hash.get(value)));
		        			}
		            		
		        			// If OUTPUT variable, add constants to inTerms
		        			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT)) {
		        				String variableType = mode.getArguments().get(i).getType();
		        				if(!newInTerms.containsKey(variableType)) {
		        					newInTerms.put(variableType, new HashSet<String>());
		        				}
		        				newInTerms.get(variableType).add(value);
		        			}
		        		}
		        		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		        		clause.addNegativeLiteral(literal);
		        		solutionsCounter++;
					}
	            }
			}
			
			// Add new terms to inTerms
			Iterator<Map.Entry<String,Set<String>>> newInTermsIterator = newInTerms.entrySet().iterator();
		    while (newInTermsIterator.hasNext()) {
		        Map.Entry<String,Set<String>> pair = (Map.Entry<String,Set<String>>)newInTermsIterator.next();
		        String variableType = pair.getKey();
		        if (!inTerms.containsKey(variableType)) {
		        	inTerms.put(variableType, new HashSet<String>());
		        }
		        inTerms.get(variableType).addAll(newInTerms.get(variableType));
		    }
		}
		
		return clause;
	}
	
	/*
	 * Bottom clause generation, querying the database only once for each relation, instead of multiple times (as done in the original algorithm)
	 */
	public MyClause generateBottomClauseSingleQueries(GenericDAO genericDAO, Tuple exampleTuple, Schema schema, Mode modeH, List<Mode> modesB, int iterations) throws Exception {
		MyClause clause = new MyClause();
		
		Map<String,String> hash = new HashMap<String,String>();
		Map<String,Set<String>> inTerms = new HashMap<String, Set<String>>();
		
		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new Exception("Example arity does not match modeH arity.");
		}
		
		// Create head literal
		List<Term> headTerms = new ArrayList<Term>();
		int varCounter = 0;
		for (int i = 0; i < modeH.getArguments().size(); i++) {
			String value = exampleTuple.getValues().get(i);
			if (modeH.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				headTerms.add(new Constant(value));
			} else {
				// INPUT or OUTPUT type
				if(!hash.containsKey(value)) {
					hash.put(value, Commons.newVariable(varCounter));
					varCounter++;
				}
				headTerms.add(new Variable(hash.get(value)));
			}
			// Add constants to inTerms
			if (modeH.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
				String variableType = modeH.getArguments().get(i).getType();
				if(!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).add(value);
			}
		}
		Predicate headLiteral = new Predicate(modeH.getPredicateName(), headTerms);
		clause.addPositiveLiteral(headLiteral);
		
		// Group modes by relation name
		Map<String,List<Mode>> groupedModes = new HashMap<String, List<Mode>>();
		for (Mode mode : modesB) {
			if(!groupedModes.containsKey(mode.getPredicateName())) {
				groupedModes.put(mode.getPredicateName(), new LinkedList<Mode>());
			}
			groupedModes.get(mode.getPredicateName()).add(mode);
		}
		
		// Create body literals
		for (int j = 0; j < iterations; j++) {
			
			// Group the WHERE expressions of each mode into relations
			Map<String, List<String>> relationsWhereExpressions = new HashMap<String, List<String>>();
			for (Mode mode : modesB) {
				String expression = computeWhereExpression(mode, schema, inTerms);
				if (!expression.isEmpty()) {
					if(!relationsWhereExpressions.containsKey(mode.getPredicateName())) {
						relationsWhereExpressions.put(mode.getPredicateName(), new LinkedList<String>());
					}
					relationsWhereExpressions.get(mode.getPredicateName()).add(expression);
				}
			}

			// Create new inTerms to hold terms for this iteration
			Map<String,Set<String>> newInTerms = new HashMap<String, Set<String>>();

			Iterator<Entry<String, List<String>>> expressionsIterator = relationsWhereExpressions.entrySet().iterator();
		    while (expressionsIterator.hasNext()) {
		        Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>)expressionsIterator.next();
		        String relation = pair.getKey();
		        List<String> expressions = pair.getValue();
			
		        // Create query
		        String query = "SELECT * FROM " + relation + " WHERE ";
		        for (int i = 0; i < expressions.size(); i++) {
					query += expressions.get(i);
					if (i < expressions.size() - 1) {
						query += " OR ";
					}
				}
		        query += ";";
		        
		        // Run query
	            GenericTableObject result = genericDAO.executeQuery(query);
	            
	            if (result != null) {
	            	for (Tuple row : result.getTable()) {
	            		// Evaluate each mode of current relation
		            	for (Mode mode : groupedModes.get(relation)) {
		            		List<Term> terms = new ArrayList<Term>();
			            	for (int i = 0; i < mode.getArguments().size(); i++) {
			            		String value = row.getValues().get(i);
			            		
			            		if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
			            			terms.add(new Constant(value));
			        			} else {
			        				// INPUT or OUTPUT type
			        				if(!hash.containsKey(value)) {
			        					hash.put(value, Commons.newVariable(varCounter));
			        					varCounter++;
			        				}
			        				terms.add(new Variable(hash.get(value)));
			        			}
			            		
			        			// If OUTPUT variable, add constants to inTerms
			        			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT)) {
			        				String variableType = mode.getArguments().get(i).getType();
			        				if(!newInTerms.containsKey(variableType)) {
			        					newInTerms.put(variableType, new HashSet<String>());
			        				}
			        				newInTerms.get(variableType).add(value);
			        			}
			        		}
			        		Predicate literal = new Predicate(mode.getPredicateName(), terms);
			        		clause.addNegativeLiteral(literal);
						}
	            	}
	            }
			}
			
			// Add new terms to inTerms
			Iterator<Map.Entry<String,Set<String>>> newInTermsIterator = newInTerms.entrySet().iterator();
		    while (newInTermsIterator.hasNext()) {
		        Map.Entry<String,Set<String>> pair = (Map.Entry<String,Set<String>>)newInTermsIterator.next();
		        String variableType = pair.getKey();
		        if (!inTerms.containsKey(variableType)) {
		        	inTerms.put(variableType, new HashSet<String>());
		        }
		        inTerms.get(variableType).addAll(newInTerms.get(variableType));
		    }
		}
		
		return clause;
	}
	
	/*
	 * Compute the WHERE expression to be used in an SQL query
	 */
	private String computeWhereExpression(Mode mode, Schema schema, Map<String,Set<String>> inTerms) {
		String expression = "";
		
		int inputVarPosition = 0;
		for (int i = 0; i < mode.getArguments().size(); i++) {
			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
				inputVarPosition = i;
				break;
			}
		}
		
		String attributeName = schema.getRelations().get(mode.getPredicateName()).getAttributeNames().get(inputVarPosition);
		String attributeType = mode.getArguments().get(inputVarPosition).getType();
		
		// If there is no list of known terms for attributeType, skip mode
		if (inTerms.containsKey(attributeType)) {
			String knownTerms = toListString(inTerms.get(attributeType));
			expression += attributeName + " IN " + knownTerms;
		}
		return expression;
	}
	
	/*
	 * Convert set to string "('item1','item2',...)"
	 */
	private String toListString(Set<String> terms) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		int counter = 0;
		for (String term : terms) {
			builder.append("'" + term + "'");
			if (counter < terms.size() - 1) {
				builder.append(",");
			}
			counter++;
		}
		builder.append(")");
		return builder.toString();
	}
}
