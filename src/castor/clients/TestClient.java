package castor.clients;

import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.language.Relation;
import castor.language.Schema;

public class TestClient {

	public static void main(String[] args) {
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	// Create data access objects and set URL of data
        	try {
        		daoFactory.initConnection("localhost");
        	}
        	catch (RuntimeException e) {
        		System.err.println("Unable to connect");
        		return;
        	}
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
    		Schema schema = genericDAO.getSchema();
    		
    		for (Relation r : schema.getRelations().values()) {
				System.out.println(r.getName() + ": " + r.getAttributeNames().toString());
			}
    		
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	// Close connection to DBMS
        	daoFactory.closeConnection();
        }
	}

}
