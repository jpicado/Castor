package castor.algorithms.bottomclause;

import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;

public interface BottomClauseGenerator {
	
	MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters);
	MyClause generateGroundBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters);
	String generateGroundBottomClauseString(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters);
}
