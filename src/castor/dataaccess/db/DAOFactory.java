package castor.dataaccess.db;

public abstract class DAOFactory {
	
	// List of DAO types supported by the factory
	public static final int VOLTDB = 1;

	public abstract void initConnection(String URL);
	public abstract void closeConnection();
	public abstract GenericDAO getGenericDAO();
	public abstract BottomClauseConstructionDAO getBottomClauseConstructionDAO();

	public static DAOFactory getDAOFactory(int whichFactory) {
		switch (whichFactory) {
		case VOLTDB:
			return new VoltDBDAOFactory();
		default:
			return null;
		}
	}
}
