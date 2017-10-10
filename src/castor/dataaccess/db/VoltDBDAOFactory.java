package castor.dataaccess.db;

public class VoltDBDAOFactory extends DAOFactory {
	
	@Override
	public void initConnection(String URL) {
		try {
			VoltDBConnectionContainer.getInstance().init(URL);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void closeConnection() {
		VoltDBConnectionContainer.getInstance().closeClient();
	}
	
	@Override
	public GenericDAO getGenericDAO() {
		return new VoltDBGenericDAO();
	}

	@Override
	public BottomClauseConstructionDAO getBottomClauseConstructionDAO() {
		return new VoltDBBottomClauseConstructionDAO();
	}
}
