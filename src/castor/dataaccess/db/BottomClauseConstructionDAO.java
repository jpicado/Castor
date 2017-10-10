package castor.dataaccess.db;


public interface BottomClauseConstructionDAO {

	public GenericTableObject executeStoredProcedure(String spName, String example, int iterations, int recall, int maxterms);
}
