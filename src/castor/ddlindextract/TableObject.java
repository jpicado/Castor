package castor.ddlindextract;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.List;
import java.util.Map;

public class TableObject {

    private String name;
    List<String> primaryKeyCols;
    Index primaryKey;
    List<ForeignKeyIndex> foreignKey;
    List<ColumnDefinition> columns;
    Map<String, Integer> ordinalPosition;


    public Map<String, Integer> getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Map<String, Integer> ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }


    public Index getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Index primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<String> getPrimaryKeyCols() {
        return primaryKeyCols;
    }

    public void setPrimaryKeyCols(List<String> primaryKeyCols) {
        this.primaryKeyCols = primaryKeyCols;
    }

    public TableObject(String name) {
        this.name = name;
    }

    public List<ForeignKeyIndex> getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(List<ForeignKeyIndex> foreignKey) {
        this.foreignKey = foreignKey;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDefinition> columns) {
        this.columns = columns;
    }


}
