package castor.dataaccess.db;

import java.util.List;

import castor.language.Tuple;

public class GenericTableObject {

	private List<Tuple> table;

	public GenericTableObject(List<Tuple> table) {
		super();
		this.table = table;
	}

	public List<Tuple> getTable() {
		return table;
	}

	public void setTable(List<Tuple> table) {
		this.table = table;
	}
}
