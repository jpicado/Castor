package castor.inputdecoders;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import castor.language.DataModel;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonDecoder {

	/*
	 * Convert JSON object for data model and convert to object
	 */
	public static DataModel decodeDataModel(JsonObject dataModelJson) throws Exception {
		String target;
		Mode modeH;
		List<Mode> modesB;
		String spName;
		
		// Read target
		if (dataModelJson.get("target") == null) {
			throw new Exception("Target not set in data model json.");
		} else {
			target = dataModelJson.get("target").getAsString();
		}
		
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
		
		return new DataModel(target, modeH, modesB, spName);
	}
	
	/*
	 * Convert JSON object for schema and convert to object
	 */
	public static Schema decodeSchema(JsonObject schemaJson) throws Exception {
		String name = "";
		Map<String, Relation> relations;
		Map<String, List<InclusionDependency>> inds;
		
		// Read schema name
		if (schemaJson.get("name") != null) {
			name = schemaJson.get("name").getAsString();
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
				String relationName = relationObject.get("name").getAsString();
				// Get relation attributes
				List<String> attributeNames = new LinkedList<String>();
				JsonArray attributesArray = relationObject.get("attributes").getAsJsonArray();
				for (int j = 0; j < attributesArray.size(); j++) {
					String attribute = attributesArray.get(j).getAsString();
					attributeNames.add(attribute);
				}
				relations.put(relationName, new Relation(relationName, attributeNames));
			}
		}
		
		// Read inclusion dependencies
		inds = new HashMap<String, List<InclusionDependency>>();
		JsonArray indsArray = schemaJson.get("inds").getAsJsonArray();
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
		
		return new Schema(name, relations, inds);
	}
}
