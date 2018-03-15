package castor.algorithms.bottomclause;

import java.util.Random;

import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;

public class BottomClauseGeneratorStreamSamplingNEW implements BottomClauseGenerator {

	private static final String SELECT_WHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";
	
	private int varCounter;
	private int seed;

	public BottomClauseGeneratorStreamSamplingNEW(int seed, GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
		this.seed = seed;
		this.varCounter = 0;
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
		
		
		MyClause clause = new MyClause();
		
		
		
		return clause;
	}
}
