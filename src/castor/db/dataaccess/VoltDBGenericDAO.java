package castor.db.dataaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import castor.language.Tuple;

public class VoltDBGenericDAO implements GenericDAO {
	
	public static final String ADHOC_QUERY = "@AdHoc";
	
	@Override
	public GenericTableObject executeQuery(String query) {
		List<Tuple> result = new ArrayList<Tuple>();
		
		try {
			// Run query
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient().callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
			VoltTable table = response.getResults()[0];
			
			// Put results in Table object
			while(table.advanceRow()) {
            	List<String> row = new ArrayList<String>();
            	for (int i = 0; i < table.getColumnCount(); i++) {
            		row.add(table.getString(i));
            	}
            	result.add(new Tuple(row));
			}
		} catch (IOException | ProcCallException e) {
			throw new RuntimeException(e);
		}
		
		return new GenericTableObject(result);
	}	
}
