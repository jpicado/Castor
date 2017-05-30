package castor.settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonSettingsReader {

	/*
	 * Read JSON object for parameters and convert to object
	 */
	public static Parameters readParameters(JsonObject parametersJson) {
		Parameters parameters = new Parameters();
			
		if (parametersJson.get("createStoredProcedure") != null) {
			parameters.setCreateStoredProcedure(parametersJson.get("createStoredProcedure").getAsBoolean());
		}
		if (parametersJson.get("minprec") != null) {
			parameters.setMinPrecision(parametersJson.get("minprec").getAsDouble());
		}
		if (parametersJson.get("minrec") != null) {
			parameters.setMinRecall(parametersJson.get("minrec").getAsDouble());
		}
		if (parametersJson.get("minpos") != null) {
			parameters.setMinPos(parametersJson.get("minpos").getAsInt());
		}
		if (parametersJson.get("noise") != null) {
			parameters.setMaxNoise(parametersJson.get("noise").getAsDouble());
		}
		if (parametersJson.get("sample") != null) {
			parameters.setSample(parametersJson.get("sample").getAsInt());
		}
		if (parametersJson.get("beam") != null) {
			parameters.setBeam(parametersJson.get("beam").getAsInt());
		}
		if (parametersJson.get("threads") != null) {
			parameters.setThreads(parametersJson.get("threads").getAsInt());
		}
		if (parametersJson.get("minimizeBottomClause") != null) {
			parameters.setMinimizeBottomClause(parametersJson.get("minimizeBottomClause").getAsBoolean());
		}
		if (parametersJson.get("reductionMethod") != null) {
			parameters.setReductionMethod(parametersJson.get("reductionMethod").getAsString());
		}
		if (parametersJson.get("iterations") != null) {
			parameters.setIterations(parametersJson.get("iterations").getAsInt());
		}
		if (parametersJson.get("recall") != null) {
			parameters.setRecall(parametersJson.get("recall").getAsInt());
		}
		if (parametersJson.get("maxterms") != null) {
			parameters.setMaxterms(parametersJson.get("maxterms").getAsInt());
		}
		if (parametersJson.get("useInds") != null) {
			parameters.setUseInds(parametersJson.get("useInds").getAsBoolean());
		}
		if (parametersJson.get("dbURL") != null) {
			parameters.setDbURL(parametersJson.get("dbURL").getAsString());
		}
		if (parametersJson.get("randomSeed") != null) {
			parameters.setRandomSeed(parametersJson.get("randomSeed").getAsInt());
		}
		
		return parameters;
	}
	
	/*
	 * Read JSON object for data model and convert to object
	 */
	public static DataModel readDataModel(JsonObject dataModelJson) throws Exception {
		Mode modeH;
		List<Mode> modesB;
		String spName;
		
		// Read head mode
		if (dataModelJson.get("headMode") == null) {
			throw new Exception("Head mode not set in data model json.");
		} else {
			String modeHString = dataModelJson.get("headMode").getAsString();
			modeH = Mode.stringToMode(modeHString);
		}
		
		// Read body modes
		if (dataModelJson.get("bodyModes") == null) {
			throw new Exception("Body modes not set in data model json.");
		} else {
			modesB = new LinkedList<Mode>();
			JsonArray modesBArray = dataModelJson.get("bodyModes").getAsJsonArray();
			for (int i = 0; i < modesBArray.size(); i++) {
				String modebString = modesBArray.get(i).getAsString();
				modesB.add(Mode.stringToMode(modebString));
			}
		}
		
		// Read stored prodecure name
		if (dataModelJson.get("spName") == null) {
			throw new Exception("Stored procedure name not set in data model json.");
		} else {
			spName = dataModelJson.get("spName").getAsString();
		}
		
		return new DataModel(modeH, modesB, spName);
	}
	
	/*
	 * Read JSON object for schema and convert to object
	 */
	public static Schema readSchema(JsonObject schemaJson) throws Exception {
		String name = "";
		Map<String, Relation> relations;
		Map<String, List<InclusionDependency>> inds;
		
		// Read schema name
		if (schemaJson.get("name") != null) {
			name = schemaJson.get("name").getAsString().toUpperCase();
		}
		
		// Read relations
		if (schemaJson.get("relations") == null) {
			throw new Exception("Schema not set in schema json.");
		} else {
			relations = new HashMap<String, Relation>();
			JsonArray relationsArray = schemaJson.get("relations").getAsJsonArray();
			for (int i = 0; i < relationsArray.size(); i++) {
				JsonObject relationObject = relationsArray.get(i).getAsJsonObject();
				// Get relation name
				String relationName = relationObject.get("name").getAsString().toUpperCase();
				// Get relation attributes
				List<String> attributeNames = new LinkedList<String>();
				JsonArray attributesArray = relationObject.get("attributes").getAsJsonArray();
				for (int j = 0; j < attributesArray.size(); j++) {
					String attribute = attributesArray.get(j).getAsString().toUpperCase();
					attributeNames.add(attribute);
				}
				relations.put(relationName, new Relation(relationName, attributeNames));
			}
		}
		
		// Read inclusion dependencies
		inds = JsonSettingsReader.readINDs(schemaJson);
		
		return new Schema(name, relations, inds);
	}
	
	/*
	 * Read JSON object for schema and convert to object
	 */
	public static Map<String, List<InclusionDependency>> readINDs(JsonObject indsJson) throws Exception {
		Map<String, List<InclusionDependency>> inds;
		
		// Read inclusion dependencies
		inds = new HashMap<String, List<InclusionDependency>>();
		JsonArray indsArray = indsJson.get("inds").getAsJsonArray();
		for (int i = 0; i < indsArray.size(); i++) {
			JsonObject indObject = indsArray.get(i).getAsJsonObject();
			String leftRelation = indObject.get("leftRelation").getAsString();
			int leftAttributeNumber = indObject.get("leftAttributeNumber").getAsInt();
			String rightRelation = indObject.get("rightRelation").getAsString();
			int rightAttributeNumber = indObject.get("rightAttributeNumber").getAsInt();
			InclusionDependency ind = new InclusionDependency(leftRelation, leftAttributeNumber, rightRelation, rightAttributeNumber);
			
			// Add ind to list of inds (grouped by left relation name)
			if (!inds.containsKey(leftRelation)) {
				inds.put(leftRelation, new LinkedList<InclusionDependency>());
			}
			inds.get(leftRelation).add(ind);
		}
		
		return inds;
	}
}
