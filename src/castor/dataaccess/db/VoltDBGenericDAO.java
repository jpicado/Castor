package castor.dataaccess.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.Pair;

public class VoltDBGenericDAO implements GenericDAO {

	public static final String ADHOC_QUERY = "@AdHoc";
	public static final String CATALOG_QUERY = "@SystemCatalog";

	@Override
	public GenericTableObject executeQuery(String query) {
		List<Tuple> result = new ArrayList<Tuple>();

		try {
			// Run query
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient()
					.callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
			VoltTable table = response.getResults()[0];

			// Put results in Table object
			while (table.advanceRow()) {
				List<Object> row = new ArrayList<Object>();
				for (int i = 0; i < table.getColumnCount(); i++) {
					row.add(table.get(i, table.getColumnType(i)));
				}
				result.add(new Tuple(row));
			}
		} catch (IOException | ProcCallException e) {
			throw new RuntimeException(e);
		}

		return new GenericTableObject(result);
	}
	
	@Override
	public GenericTableObject executeQueryWithSelectLimit(String query, List<Pair<Integer,Object>> selectConditions, int limit) {
		List<Tuple> result = new ArrayList<Tuple>();

		try {
			// Run query
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient()
					.callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
			VoltTable table = response.getResults()[0];

			// Put results in Table object
			int resultsCounter = 0;
			while (table.advanceRow()) {
				List<Object> row = new ArrayList<Object>();
				for (int i = 0; i < table.getColumnCount(); i++) {
					row.add(table.get(i, table.getColumnType(i)));
				}
				
				boolean keep = true;
				for (Pair<Integer,Object> selectCondition : selectConditions) {
					int attributePosition = selectCondition.getFirst();
					Object desiredValue = selectCondition.getSecond();
					if (!row.get(attributePosition).equals(desiredValue)) {
						keep = false;
						break;
					}
				}
				
				if (keep) {
					result.add(new Tuple(row));
					resultsCounter++;
					
					if (resultsCounter == limit)
						break;
				}
			}
		} catch (IOException | ProcCallException e) {
			throw new RuntimeException(e);
		}

		return new GenericTableObject(result);
	}

	@Override
	public long executeScalarQuery(String query) {
		long result = 0;
		try {
			// Run query
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient()
					.callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
			VoltTable table = response.getResults()[0];

			// Get result
			table.advanceRow();
			result = table.getLong(0);
		} catch (IOException | ProcCallException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public Schema getSchema() {
		Map<String, Relation> relations = new HashMap<String, Relation>();

		try {
			// Run query
			ClientResponse response = VoltDBConnectionContainer.getInstance().getClient()
					.callProcedure(VoltDBGenericDAO.CATALOG_QUERY, "COLUMNS");
			VoltTable table = response.getResults()[0];

			while (table.advanceRow()) {
				String relationName = table.getString("TABLE_NAME").toUpperCase();
				String attributeName = table.getString("COLUMN_NAME").toUpperCase();
				int attributeOrdinalPosition = (int) table.getLong("ORDINAL_POSITION");

				if (!relations.containsKey(relationName)) {
					relations.put(relationName, new Relation(relationName, new ArrayList<String>()));
				}

				// If list of attributes is not big enough, insert dummy attributes
				if (relations.get(relationName).getAttributeNames().size() < attributeOrdinalPosition) {
					for (int i = relations.get(relationName).getAttributeNames()
							.size(); i < attributeOrdinalPosition; i++) {
						relations.get(relationName).getAttributeNames().add(null);
					}
				}

				// Insert attribute in correct position
				relations.get(relationName).getAttributeNames().set(attributeOrdinalPosition - 1, attributeName);
			}
		} catch (IOException | ProcCallException e) {
			throw new RuntimeException(e);
		}

		return new Schema(relations);
	}

}
