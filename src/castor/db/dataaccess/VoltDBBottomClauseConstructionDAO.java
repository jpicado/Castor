package castor.db.dataaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

import castor.language.Tuple;

public class VoltDBBottomClauseConstructionDAO implements BottomClauseConstructionDAO {

	@Override
	public GenericTableObject executeStoredProcedure(String spName, String example,
			int iterations, int recall, int maxterms) {
		List<Tuple> result = new ArrayList<Tuple>();
		
		try {
			// Run stored procedure
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient().callProcedure(spName, example, iterations, recall, maxterms);
			VoltTable table = response.getResults()[0];
			
			// Put results in Table object
			while(table.advanceRow()) {
            	List<String> row = new ArrayList<String>();
            	for (int i = 0; i < table.getColumnCount(); i++) {
            		row.add(table.getString(i));
            	}
            	result.add(new Tuple(row));
			}
		} catch(ProcCallException|IOException e) {
			throw new RuntimeException(e);
		}
		
		return new GenericTableObject(result);
	}
}
