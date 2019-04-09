package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.sampling.JoinNode;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;
import castor.utils.Triple;

public abstract class BottomClauseGeneratorUsingJoinTree implements BottomClauseGenerator {

	protected static final String SELECT_WHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s";
	private static final String NULL_PREFIX = "null";
	
	private int varCounter;
	private int seed;
	private JoinNode joinTree;
	private int nullCounter;

	public BottomClauseGeneratorUsingJoinTree(int seed, JoinNode joinTree) {
		this.seed = seed;
		this.varCounter = 0;
		this.joinTree = joinTree;
		this.nullCounter = 0;
	}
	
	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel,
				parameters, false);
	}

	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel,
				parameters, true);
	}

	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		MyClause clause = generateGroundBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema,
				dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}
	
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters, boolean ground) {
		Random randomGenerator = new Random(seed);
		
		// Check that arities of example and modeH match
		if (dataModel.getModeH().getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		int sampleSize;
		if (ground)
			sampleSize = parameters.getGroundRecall();
		else
			sampleSize = parameters.getRecall();
		
		varCounter = 0;
		MyClause clause = new MyClause();
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		
		// <relation,depth,tuple> -> size
		// need relation in case the same tuple appears in multiple relations
		// need depth in case a relation appears at different depths in the join tree
		Map<Triple<String,Integer,Tuple>,Long> joinPathSizes = new HashMap<Triple<String,Integer,Tuple>,Long>();
		
		// Create head literal
		Mode modeH = dataModel.getModeH();
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, exampleTuple, modeH, true);
		clause.addPositiveLiteral(headLiteral);
		
		// Group modes by relation name
		Map<String, List<Mode>> groupedModes = new LinkedHashMap<String, List<Mode>>();
		for (Mode mode : dataModel.getModesB()) {
			if (!groupedModes.containsKey(mode.getPredicateName())) {
				groupedModes.put(mode.getPredicateName(), new LinkedList<Mode>());
			}
			groupedModes.get(mode.getPredicateName()).add(mode);
		}
		
		createBodyLiterals(genericDAO, schema, joinTree, exampleTuple, groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, sampleSize, parameters.getQueryLimit());
		
		return clause;
	}
	
	abstract protected void createBodyLiterals(GenericDAO genericDAO, Schema schema, 
			JoinNode joinTree, Tuple tuple, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int sampleSize, int queryLimit);
	
	protected void modeOperationsForTuple(Tuple tuple, MyClause clause, 
			Map<String, String> hashConstantToVariable, 
			List<Mode> relationAttributeModes, boolean ground) {
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : relationAttributeModes) {
			if (ground) {
				if (usedModes.contains(mode.toGroundModeString())) {
					continue;
				}
				else {
					mode = mode.toGroundMode();
					usedModes.add(mode.toGroundModeString());
				}
			}
			
			Predicate newLiteral = createLiteralFromTuple(hashConstantToVariable, tuple, mode, false);
			
			// Do not add literal if it's exactly the same as head literal
			if (!newLiteral.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
				clause.addNegativeLiteral(newLiteral);
			}
		}
	}
	
	/*
	 * Creates a literal from a tuple and a mode.
	 */
	private Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable, Tuple tuple, Mode mode, boolean headMode) {
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < mode.getArguments().size(); i++) {
			String value;
			if (tuple.getValues().get(i) != null) {
				value = tuple.getValues().get(i).toString();
			}
			else {
				value = NULL_PREFIX+nullCounter;
				nullCounter++;
			}

			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				terms.add(new Constant("\"" + Commons.escapeMetaCharacters(value) + "\""));
			} else {
				// INPUT or OUTPUT type
				if (!hashConstantToVariable.containsKey(value)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(value, var);
				}
				terms.add(new Variable(hashConstantToVariable.get(value)));
			}
		}

		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}
}
