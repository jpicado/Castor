package castor.ddlindextract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import castor.utils.Constants;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.create.table.Index;

public class DDLParser {
	final static Logger logger = Logger.getLogger(DDLParser.class);

	public static Map<String, TableObject> parseDDLFile(String file) {

		Stream<String> stream = null;
		StringBuilder sb = new StringBuilder();
		Map<String, TableObject> tableMap = null;
		try {
			stream = Files.lines(Paths.get(file));
			{
				stream.filter(s -> !s.isEmpty()).forEach(s -> sb.append(s.toLowerCase()));
			}
		} catch (IOException e) {
			logger.error("Error while processing tranformation file");
			e.printStackTrace();
		}
		stream.close();

		String ddlFileString = sb.toString();

		String[] ddlSql = ddlFileString.split(Constants.Regex.SEMICOLON.getValue());

		for (String sql : ddlSql) {
			if (sql.trim().startsWith(Constants.DDLString.CREATE.getValue())) {
				if (tableMap == null) {
					// Using LinkedHashMap to keep order in file
					tableMap = new LinkedHashMap<String, TableObject>();
				}

				try {
					String parseSql = DDLParser.removeUnwantedChars(sql.trim());
					CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(parseSql);

					TableObject tableObject = new TableObject(createTable.getTable().getName());
					List<ForeignKeyIndex> foreignKeyList = new ArrayList<ForeignKeyIndex>();

					tableObject.setColumns(createTable.getColumnDefinitions());
					DDLParser.setColumnsOrdinalPosition(tableObject);

					List<Index> indexes = createTable.getIndexes();
					for (Index index : indexes) {
						if (index.getType().equals(Constants.IndexType.PRIMARY_KEY.getValue())) {
							tableObject.setPrimaryKey(index);
							tableObject.setPrimaryKeyCols(index.getColumnsNames());
						} else {
							if (index.getType().equals(Constants.IndexType.FOREGIN_KEY.getValue())) {
								foreignKeyList.add((ForeignKeyIndex) index);
							}
						}
					}

					tableObject.setForeignKey(foreignKeyList);
					tableMap.put(tableObject.getName(), tableObject);

				} catch (JSQLParserException e) {
					e.printStackTrace();
				}
			}
		}

		return tableMap;
	}

	private static String removeUnwantedChars(String sql) {
		String prefixSql = sql.substring(0, sql.indexOf(Constants.Regex.OPEN_PARENTHESIS.getValue()));
		String postfixSql = (String) sql.subSequence(sql.indexOf(Constants.Regex.OPEN_PARENTHESIS.getValue()) + 1,
				sql.length() - 1);

		String[] attributes = postfixSql.split(Constants.DDLRegex.DDL_SPLITTER.getValue());
		StringBuilder sb = new StringBuilder();
		sb.append(prefixSql + Constants.Regex.OPEN_PARENTHESIS.getValue());

		int count = 0;
		for (String attribute : attributes) {
			if (attribute.trim().startsWith(Constants.DDLString.CONSTRAINT.getValue())) {
				String value = null;
				if (attribute.contains(Constants.DDLString.PRIMARY.getValue())) {
					value = getPrimaryKeyString(attribute.trim());
				} else if (attribute.contains(Constants.DDLString.FOREGIN.getValue())) {
					value = getForeignKeyString(attribute.trim());
				}

				if (value == Constants.Strings.UNCHANGED.getValue())
					sb.append(attribute.trim());
				else
					sb.append(value);

			} else {
				sb.append(attribute.trim());
			}

			count++;
			if (count != attributes.length)
				sb.append(Constants.Regex.COMMA.getValue());
			else
				sb.append(Constants.Regex.CLOSE_PARENTHESIS.getValue());
		}
		return sb.toString();
	}

	private static String getPrimaryKeyString(String input) {
		String format = Constants.DDLRegex.PK_REGEX.getValue();
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(input);
		boolean match = matcher.matches();
		if (match == true)
			return Constants.Strings.UNCHANGED.getValue();
		else {
			Matcher invalMatch = pattern.matcher(input);
			while (invalMatch.find()) {
				return invalMatch.group();
			}
		}
		return null;
	}

	private static String getForeignKeyString(String input) {
		String format = Constants.DDLRegex.FK_REGEX.getValue();
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(input);
		boolean match = matcher.matches();
		if (match == true)
			return Constants.Strings.UNCHANGED.getValue();
		else {
			Matcher invalMatch = pattern.matcher(input);
			while (invalMatch.find()) {
				return invalMatch.group();
			}
		}
		return null;

	}

	private static void setColumnsOrdinalPosition(TableObject tableObject) {
		List<ColumnDefinition> colList = tableObject.getColumns();
		Map<String, Integer> ordinalPosition = new HashMap<String, Integer>();
		int count = 0;
		for (ColumnDefinition col : colList) {
			ordinalPosition.put(col.getColumnName(), count);
			count++;
		}
		tableObject.setOrdinalPosition(ordinalPosition);
	}
}
