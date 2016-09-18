package castor.db.dataaccess;

import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;

public class VoltDBConnectionContainer {

	private static VoltDBConnectionContainer INSTANCE;
	private Client client;
	
	public static VoltDBConnectionContainer getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new VoltDBConnectionContainer();
		}
		return INSTANCE;
	}
	
	public void init(String URL) throws Exception {
		client = ClientFactory.createClient();
		client.createConnection(URL);
	}
	
	public Client getClient() {
		if (client == null) {
			throw new IllegalStateException("Client has not been initialized. Call VoltDBConnectionContainer.init(URL) to initialize.");
		}
		return client;
	}
	
	public void closeClient() {
		try {
			if (client != null) {
				client.close();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private VoltDBConnectionContainer() {
	}
	
	
}
