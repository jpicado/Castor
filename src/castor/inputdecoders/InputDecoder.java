package castor.inputdecoders;

import java.util.List;
import java.util.Map;

import castor.language.Determination;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Schema;

public interface InputDecoder {

	void readSchemaFileFromXml(String path);
	boolean readSchemaFileFromSql(String path);
	boolean readUserInputFile(String inputPath);
	String getServerName();
	String getTemporaryTableName();
	String getSPNameTemplate();
	int getIterations();
	int getRecall();
	int getMaxTerms();
	Mode getModeH();
	List<Mode> getModesB();
	List<Determination> getDeterminations();
	Schema getSchema();
	Map<String, List<InclusionDependency>> getInclusionDep();
}
