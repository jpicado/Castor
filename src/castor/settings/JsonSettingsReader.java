package castor.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import castor.language.InclusionDependency;
import castor.language.MatchingDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.utils.Constants;
import castor.utils.Pair;

public class JsonSettingsReader {

	/*
	 * Read JSON object for parameters and convert to object
	 */
	public static Parameters readParameters(JsonObject parametersJson) {
		Parameters parameters = new Parameters();

		if (parametersJson.get("estimationSample") != null) {
			parameters.setEstimationSample(parametersJson.get("estimationSample").getAsDouble());
		}
		if (parametersJson.get("generalizationMethod") != null) {
			parameters.setGeneralizationMethod(parametersJson.get("generalizationMethod").getAsString());
		}
		if (parametersJson.get("createStoredProcedure") != null) {
			parameters.setCreateStoredProcedure(parametersJson.get("createStoredProcedure").getAsBoolean());
		}
		if (parametersJson.get("useStoredProcedure") != null) {
			parameters.setUseStoredProcedure(parametersJson.get("useStoredProcedure").getAsBoolean());
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
		if (parametersJson.get("groundRecall") != null) {
			parameters.setGroundRecall(parametersJson.get("groundRecall").getAsInt());
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
		if (parametersJson.get("port") != null) {
			parameters.setPort(parametersJson.get("port").getAsString());
		}
		if (parametersJson.get("randomSeed") != null) {
			parameters.setRandomSeed(parametersJson.get("randomSeed").getAsInt());
		}
		if (parametersJson.get("sampling") != null) {
			parameters.setSamplingMethod(parametersJson.get("sampling").getAsString());
		}
		if (parametersJson.get("sampleInTesting") != null) {
			parameters.setSampleInTesting(parametersJson.get("sampleInTesting").getAsBoolean());
		}
		if (parametersJson.get("sampleGroundBottomClauses") != null) {
			parameters.setSampleGroundBottomClauses(parametersJson.get("sampleGroundBottomClauses").getAsBoolean());
		}
		if (parametersJson.get("sampleInCoveringApproach") != null) {
			parameters.setSampleInCoveringApproach(parametersJson.get("sampleInCoveringApproach").getAsBoolean());
		}
		if (parametersJson.get("shuffleExamples") != null) {
			parameters.setShuffleExamples(parametersJson.get("shuffleExamples").getAsBoolean());
		}
		if (parametersJson.get("randomizeRecall") != null) {
			parameters.setRandomizeRecall(parametersJson.get("randomizeRecall").getAsBoolean());
		}
		if (parametersJson.get("allowSimilarity") != null) {
			parameters.setAllowSimilarity(parametersJson.get("allowSimilarity").getAsBoolean());
		}
		
		return parameters;
	}
	
	/*
	 * Read JSON object for grid search parameters
	 */
	public static Map<String,List<Double>> readGridSearchParameters(JsonObject parametersJson) {
		Map<String,List<Double>> parameters = new HashMap<String,List<Double>>();
		
		for(Map.Entry<String,JsonElement> entry : parametersJson.entrySet()) {
    		String parameterName = entry.getKey();
    		JsonArray valuesArray = entry.getValue().getAsJsonArray();
    		List<Double> valuesList = new LinkedList<Double>();
    		for (int i = 0; i < valuesArray.size(); i++) {
    			valuesList.add(valuesArray.get(i).getAsDouble());
    		}
    		parameters.put(parameterName, valuesList);
    	}
		
		return parameters;
	}
	
	/*
	 * Read JSON object for data model and convert to object
	 */
	public static DataModel readDataModel(JsonObject dataModelJson) {
		Mode modeH;
		List<Mode> modesB;
		String spName;

		// Read head mode
		if (dataModelJson.get("headMode") == null) {
			throw new IllegalArgumentException("Head mode not set in data model json.");
		} else {
			String modeHString = dataModelJson.get("headMode").getAsString();
			modeH = Mode.stringToMode(modeHString);
		}
		
		// Read body modes
		if (dataModelJson.get("bodyModes") == null) {
			throw new IllegalArgumentException("Body modes not set in data model json.");
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
			throw new IllegalArgumentException("Stored procedure name not set in data model json.");
		} else {
			spName = dataModelJson.get("spName").getAsString();
		}
		
		return new DataModel(modeH, modesB, spName);
	}


	/*
 * Read JSON object for data model and convert to object
 */
	public static DataModel readDataModelForTransformation(JsonObject dataModelJson) {
		Mode modeH;
		List<Mode> modesB;
		Map<String, List<List<String>>> modesBMap;
		String spName;

		// Read head mode
		if (dataModelJson.get("headMode") == null) {
			throw new IllegalArgumentException("Head mode not set in data model json.");
		} else {
			String modeHString = dataModelJson.get("headMode").getAsString();
			modeH = Mode.stringToMode(modeHString);
		}

		// Read body modes
		if (dataModelJson.get("bodyModes") == null) {
			throw new IllegalArgumentException("Body modes not set in data model json.");
		} else {
			modesB = new LinkedList<Mode>();
			modesBMap = new HashMap<String, List<List<String>>>();
			JsonArray modesBArray = dataModelJson.get("bodyModes").getAsJsonArray();
			for (int i = 0; i < modesBArray.size(); i++) {
				String modebString = modesBArray.get(i).getAsString();
				modesB.add(Mode.stringToMode(modebString));
				initializeModesBodyMap(modebString, modesBMap);
			}
		}

		// Read stored prodecure name
		if (dataModelJson.get("spName") == null) {
			throw new IllegalArgumentException("Stored procedure name not set in data model json.");
		} else {
			spName = dataModelJson.get("spName").getAsString();
		}

		return new DataModel(modeH, modesB, modesBMap, spName);
	}

	//Add the attribute type information to jsonmodel, add #attribute directly. Remove +/- from string
	public static void initializeModesBodyMap(String modebString, Map<String, List<List<String>>> modesBMap) {
		String relationName = modebString.substring(0, modebString.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
		String attributes = (String) modebString.subSequence(modebString.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, modebString.indexOf(Constants.TransformDelimeter.CLOSE_PARA.getValue()));
		String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());
		List<List<String>> modeList = null;
		if (!modesBMap.containsKey(relationName)) {
			modeList = new ArrayList<List<String>>();
		} else {
			modeList = modesBMap.get(relationName);
		}
		List<String> attributesList = new ArrayList<>();
		for (String attribute : attributeArray) {
			attributesList.add(attribute);
		}
		modeList.add(attributesList);
		modesBMap.put(relationName, modeList);
	}


	/*
	 * Read JSON object for schema and convert to object
	 */
	public static Schema readSchema(JsonObject schemaJson) {
		String name = "";
		Map<String, Relation> relations;
		Map<String, List<InclusionDependency>> inds;
		
		// Read schema name
		if (schemaJson.get("name") != null) {
			name = schemaJson.get("name").getAsString().toUpperCase();
		}
		
		// Read relations
		if (schemaJson.get("relations") == null) {
			throw new IllegalArgumentException("Schema not set in schema json.");
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
	 * Read JSON object for INDs and convert to object
	 */
	public static Map<String, List<InclusionDependency>> readINDs(JsonObject dependenciesJson)  {
		Map<String, List<InclusionDependency>> inds = new HashMap<String, List<InclusionDependency>>();
		
		try {
			if (dependenciesJson.get("inds") != null) {
				JsonArray indsArray = dependenciesJson.get("inds").getAsJsonArray();
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
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error while reading inclusion dependencies: " + ex.getMessage());
		}
		
		return inds;
	}
	
	/*
	 * Read JSON object for MDs and convert to object
	 */
	public static Map<Pair<String,Integer>, List<MatchingDependency>> readMDs(JsonObject dependenciesJson)  {
		Map<Pair<String,Integer>, List<MatchingDependency>> mds = new HashMap<Pair<String,Integer>, List<MatchingDependency>>();
		
		try {
			if (dependenciesJson.get("mds") != null) {
				JsonArray mdsArray = dependenciesJson.get("mds").getAsJsonArray();
				for (int i = 0; i < mdsArray.size(); i++) {
					JsonObject mdObject = mdsArray.get(i).getAsJsonObject();
					String leftRelation = mdObject.get("leftRelation").getAsString();
					int leftAttributeNumber = mdObject.get("leftAttributeNumber").getAsInt();
					String rightRelation = mdObject.get("rightRelation").getAsString();
					int rightAttributeNumber = mdObject.get("rightAttributeNumber").getAsInt();
					double maxDistance = mdObject.get("maxDistance").getAsDouble();
					MatchingDependency md = new MatchingDependency(leftRelation, leftAttributeNumber, rightRelation, rightAttributeNumber, maxDistance);
					
					// Add md to list of mds (grouped by <left relation name , left attribute number>)
					Pair<String,Integer> key = new Pair<String,Integer>(leftRelation, leftAttributeNumber);
					if (!mds.containsKey(key)) {
						mds.put(key, new LinkedList<MatchingDependency>());
					}
					mds.get(key).add(md);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error while reading matching dependencies: " + ex.getMessage());
		}
		
		return mds;
	}
	
	/*
	 * Read JSON object containing metadata and convert to object
	 */
	public static Map<String, ModeMetadata> readMetadata(JsonObject metadataJson) {
		Map<String, ModeMetadata> metadata = new HashMap<String, ModeMetadata>();
		JsonArray attributesArray = metadataJson.get("attributes").getAsJsonArray();
		for (int i = 0; i < attributesArray.size(); i++) {
			JsonObject attributeObject = attributesArray.get(i).getAsJsonObject();
			
			// Name
			String name = attributeObject.get("name").getAsString().toLowerCase();
			
			// Types
			List<String> types = new LinkedList<String>();
			JsonArray typesArray = attributeObject.get("types").getAsJsonArray();
			for (int j = 0; j < typesArray.size(); j++) {
				types.add(typesArray.get(j).getAsString());
			}
			
			// Access modes
			List<String> accessModes = new LinkedList<String>();
			JsonArray accessModesArray = attributeObject.get("access_modes").getAsJsonArray();
			for (int j = 0; j < accessModesArray.size(); j++) {
				accessModes.add(accessModesArray.get(j).getAsString());
			}
			
			metadata.put(name, new ModeMetadata(types, accessModes));
		}
		
		return metadata;
	}
}
