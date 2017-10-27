package castor.ddlindextract;

import castor.utils.Constants;
import castor.utils.FileUtils;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDLIndMain {

    final static Logger logger = Logger.getLogger(DDLIndMain.class);

    public static boolean extractIndFromDDL(String inputDDLFile, String outputFile) {
        logger.info("Running DDLIndExtraction ");
        Map<String, TableObject> tables = DDLParser.parseDDLFile(inputDDLFile);
        List<String> inds = new ArrayList<String>();
        List<String> invalidPks = new ArrayList<String>();
        Map<String, Integer> ordinalPos = new HashMap<String, Integer>();
        generateInds(tables, inds, invalidPks, ordinalPos);
        boolean result = FileUtils.writeIndsToJsonFormat(inds, ordinalPos, outputFile);
        if (!invalidPks.isEmpty()) {
            logger.info("Invalid primary keys found: " + invalidPks.toString());
        }
        return result;
    }

    private static void generateInds(Map<String, TableObject> tables, List<String> inds, List<String> invalidPks, Map<String, Integer> ordinalPos) {
        for (Map.Entry<String, TableObject> tableEntry : tables.entrySet()) {
            TableObject table = tableEntry.getValue();
            List<ForeignKeyIndex> fkList = table.getForeignKey();
            for (ForeignKeyIndex index : fkList) {
                isForeignKeyValid(index, tables, table, inds, invalidPks, ordinalPos);
            }
        }
    }

    //Create primary key map , table and pk column
    private static void isForeignKeyValid(ForeignKeyIndex index, Map<String, TableObject> tables, TableObject baseTable, List<String> inds, List<String> invalidPks, Map<String, Integer> ordinalPos) {
        String refTableName = index.getTable().getName();
        String baseTableName = baseTable.getName();
        List<String> refColName = index.getReferencedColumnNames();
        List<String> baseColumnName = index.getColumnsNames();

        TableObject refTable = tables.get(refTableName);
        int count = 0;
        for (String refCol : refColName) {
            if (refTable.getPrimaryKeyCols().contains(refCol)) {
                inds.add(Constants.Regex.OPEN_PARENTHESIS.getValue() + baseTableName + Constants.Regex.PERIOD.getValue() + baseColumnName.get(count) + Constants.Regex.PARENTHESIS.getValue() + refTableName + Constants.Regex.PERIOD.getValue() + refCol + Constants.Regex.CLOSE_PARENTHESIS.getValue() + "\n");
                inds.add(Constants.Regex.OPEN_PARENTHESIS.getValue() + refTableName + Constants.Regex.PERIOD.getValue() + refCol + Constants.Regex.PARENTHESIS.getValue() + baseTableName + Constants.Regex.PERIOD.getValue() + baseColumnName.get(count) + Constants.Regex.CLOSE_PARENTHESIS.getValue() + "\n");
                setOrdinalPosition(baseTable, baseColumnName.get(count), refTable, refCol, ordinalPos);
                count++;
            } else {
                invalidPks.add(refTableName + Constants.Regex.PERIOD.getValue() + refCol);
            }
        }
    }

    private static void setOrdinalPosition(TableObject baseTable, String baseColumn, TableObject refTable, String refCol, Map<String, Integer> ordinalPos) {
        Map<String, Integer> baseTableOrdinalPos = baseTable.getOrdinalPosition();
        Map<String, Integer> refTableOrdinalPos = refTable.getOrdinalPosition();
        ordinalPos.put(baseTable.getName() + Constants.Regex.PERIOD.getValue() + baseColumn, baseTableOrdinalPos.get(baseColumn));
        ordinalPos.put(refTable.getName() + Constants.Regex.PERIOD.getValue() + refCol, refTableOrdinalPos.get(refCol));
    }
}
