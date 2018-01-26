package castor.dataaccess.db;

import java.util.List;

import castor.language.Schema;
import castor.utils.Pair;


public interface GenericDAO {

	public GenericTableObject executeQuery(String query);
	GenericTableObject executeQueryWithSelectLimit(String query, List<Pair<Integer,Object>> selectConditions, int limit);
	public long executeScalarQuery(String query);
	public Schema getSchema();
}
