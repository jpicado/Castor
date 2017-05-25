package castor.db.dataaccess;

import castor.language.Schema;


public interface GenericDAO {

	public GenericTableObject executeQuery(String query);
	public long executeScalarQuery(String query);
	public Schema getSchema();
}
