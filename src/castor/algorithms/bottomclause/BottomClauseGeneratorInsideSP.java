package castor.algorithms.bottomclause;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.db.DBCommons;
import castor.hypotheses.MyClause;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;

public class BottomClauseGeneratorInsideSP implements BottomClauseGenerator {
	
	private boolean sample;
	
	public BottomClauseGeneratorInsideSP(boolean sample) {
		this.sample = sample;
	}

	/*
	 * Bottom clause generation using a single stored procedure
	 */
	// Assumes that exampleTuple is for relation with same name as modeH
	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		MyClause clause = new MyClause();
		
		// If sampling is turned off, set recall to max value
		int recall = parameters.getRecall();
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
		// Call procedure that creates bottom clause
		String example = String.join(DBCommons.ATTRIBUTE_DELIMITER, exampleTuple.getStringValues());
        GenericTableObject result = bottomClauseConstructionDAO.executeStoredProcedure(dataModel.getSpName(), example, parameters.getIterations(), recall, parameters.getMaxterms());
        
        // Process results, which should contain a single row
        if (result != null && result.getTable().size() > 0) {
        	// Each column is the string representation of a literal
        	Tuple row = result.getTable().get(0);
        	for (int i = 0; i < row.getValues().size(); i++) {
				String column = row.getValues().get(i).toString();
        		// Extract predicate name and arguments from column
        		String[] tokens = column.split("\\(", 2);
    			String predicate = tokens[0];
    			String arguments = tokens[1].substring(0, tokens[1].length()-1);
    			Matcher matcher = Pattern.compile("([^\"'][^,]+|\".+?\"|'.+?')[,\\s]*").matcher(arguments);
    			List<Term> terms = new LinkedList<Term>();
                while (matcher.find()) {
                	String term = matcher.group(1);
                	if (Commons.isVariable(term)) {
    					terms.add(new Variable(term));
    				} else {
    					terms.add(new Constant(term));
    				}
                }
				
				Predicate newPredicate = new Predicate(predicate, terms);
				if (i == 0) {
					// First literal is the head
					clause.addPositiveLiteral(newPredicate);
				} else {
					clause.addNegativeLiteral(newPredicate);
				}
			}
        }
		
		return clause;
	}
	
	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		MyClause clause = new MyClause();
		
		// If sampling is turned off, set recall to max value
		int recall = parameters.getGroundRecall();
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
		// Call procedure that creates bottom clause
		String example = String.join(DBCommons.ATTRIBUTE_DELIMITER, exampleTuple.getStringValues());
		String spName = dataModel.getSpName() + DBCommons.GROUND_BOTTONCLAUSE_PROCEDURE_SUFFIX;
        GenericTableObject result = bottomClauseConstructionDAO.executeStoredProcedure(spName, example, parameters.getIterations(), recall, parameters.getMaxterms());
        
        // Process results, which should contain a single row
        if (result != null && result.getTable().size() > 0) {
        	// Each column is the string representation of a literal
        	Tuple row = result.getTable().get(0);
        	for (int i = 0; i < row.getValues().size(); i++) {
				String column = row.getValues().get(i).toString();
        		// Extract predicate name and arguments from column
        		String[] tokens = column.split("\\(", 2);
    			String predicate = tokens[0];
    			String arguments = tokens[1].substring(0, tokens[1].length()-1);
    			Matcher matcher = Pattern.compile("([^\"'][^,]+|\".+?\"|'.+?')[,\\s]*").matcher(arguments);
    			List<Term> terms = new LinkedList<Term>();
                while (matcher.find()) {
                	String term = matcher.group(1);
                	if (Commons.isVariable(term)) {
    					terms.add(new Variable(term));
    				} else {
    					terms.add(new Constant(term));
    				}
                }
				
				Predicate newPredicate = new Predicate(predicate, terms);
				if (i == 0) {
					// First literal is the head
					clause.addPositiveLiteral(newPredicate);
				} else {
					clause.addNegativeLiteral(newPredicate);
				}
			}
        }
		
		return clause;
	}
	
	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		MyClause clause = generateGroundBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
//		StringBuilder sb = new StringBuilder();
//		
//		// If sampling is turned off, set recall to max value
//		int recall = parameters.getGroundRecall();
//		if (!this.sample) {
//			recall = Integer.MAX_VALUE;
//		}
//		
//		// Call procedure that creates ground bottom clause
//		String example = String.join(DBCommons.ATTRIBUTE_DELIMITER, exampleTuple.getStringValues());
//		String spName = dataModel.getSpName() + DBCommons.GROUND_BOTTONCLAUSE_PROCEDURE_SUFFIX;
//        GenericTableObject result = bottomClauseConstructionDAO.executeStoredProcedure(spName, example, parameters.getIterations(), recall, parameters.getMaxterms());
//        
//        // Process results, which should contain a single row
//        if (result != null && result.getTable().size() > 0) {
//        	// Each column is the string representation of a literal
//        	// First column is head of clause
//        	Tuple row = result.getTable().get(0);
//        	sb.append(row.getValues().get(0));
//        	// Skip first column, which is clause head
//        	for (int i = 1; i < row.getValues().size(); i++) {
//        		sb.append(", ");
//        		sb.append(MyClauseToIDAClause.NEGATE_SYMBOL+row.getValues().get(i));
//			}
//        }
//		
//		return sb.toString();
	}
}
