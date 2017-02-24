package castor.db.dataaccess;


public interface GenericDAO {

	public GenericTableObject executeQuery(String query);
	public long executeScalarQuery(String query);
}
