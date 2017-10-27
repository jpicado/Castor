package castor.modetransform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import castor.dataaccess.file.CSVFileReader;
import castor.ddlindextract.DDLParser;
import castor.ddlindextract.TableObject;
import castor.settings.JsonSettingsReader;
import castor.settings.ModeMetadata;
import castor.utils.FileUtils;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

public class ModeGenerator {
	
	final static Logger logger = Logger.getLogger(ModeGenerator.class);

	public static void generateModesFromMetadata(String ddlFile, String target, String examplesFile, String metadataFile, String spName, String outputModesFile) {
		
		// Read schema DDL
		Map<String, TableObject> ddlMap =  DDLParser.parseDDLFile(ddlFile);
		
		// Read relation name and header from examples file
		List<String> examplesFileHeader = CSVFileReader.readCSVHeader(examplesFile);
		
		// Read metadata
		JsonObject metadataJson = FileUtils.convertFileToJSON(metadataFile);
		Map<String, ModeMetadata> metadata = JsonSettingsReader.readMetadata(metadataJson);
		
		// Generate head mode
		String headModeBody = generateModes(examplesFileHeader, metadata, true).get(0);
		String headMode = target + "(" + headModeBody + ")";
		
		// Generate body modes
		List<String> bodyModes = new LinkedList<String>();
		for (Map.Entry<String, TableObject> entry : ddlMap.entrySet()) {
			String relationName = entry.getKey();
			TableObject tableObject = entry.getValue();
			
			List<String> attributes = new ArrayList<String>();
			for (ColumnDefinition column : tableObject.getColumns()) {
				attributes.add(column.getColumnName());
			}
			List<String> potentialModes = generateModes(attributes, metadata, false);
			
			for (String potentialMode : potentialModes) {
				// Keep only modes that have one input variables
				int inputVars = StringUtils.countMatches(potentialMode, "+");
				if (inputVars == 1) {
					String newMode = relationName + "(" + potentialMode + ")";
					bodyModes.add(newMode);
				}
			}
		}
		
		try {
			String outputFolder = FilenameUtils.getFullPathNoEndSeparator(outputModesFile);
			Files.createDirectories(Paths.get(outputFolder));
			FileUtils.writeModeToJsonFormat(null, headMode, bodyModes, spName+"_"+target, outputModesFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		logger.info("Modes written to file " + outputModesFile);
	}
	
	private static List<String> generateModes(List<String> attributes, Map<String, ModeMetadata> metadata, boolean headMode) {
		return generateModesAux(0, attributes, metadata, "", headMode);
	}
	
	private static List<String> generateModesAux(int currentAttribute, List<String> attributes, Map<String, ModeMetadata> metadata, String modeBody, boolean headMode) {
		List<String> modes = new LinkedList<String>();
		
		if (currentAttribute >= attributes.size()) {
			modes.add(modeBody);
		} else {
			String attribute = attributes.get(currentAttribute);
			
			if (!metadata.containsKey(attribute)) {
				throw new IllegalArgumentException("Metadata for attribute " + attribute + " not available.");
			}
			
			List<String> types = metadata.get(attribute).getTypes();
			List<String> accessModes = metadata.get(attribute).getAccessModes();
			
			Collections.sort(types);
			
			for (String type : types) {
				if (headMode || accessModes.contains("+")) {
					String newModeBody = (modeBody.equals("")) ? "+"+type : modeBody+",+"+type;
					modes.addAll(generateModesAux(currentAttribute+1, attributes, metadata, newModeBody, headMode));
				}
				if (!headMode && accessModes.contains("#")) {
					String newModeBody = (modeBody.equals("")) ? "#"+type : modeBody+",#"+type;
					modes.addAll(generateModesAux(currentAttribute+1, attributes, metadata, newModeBody, headMode));
				}
				if (!headMode && accessModes.contains("-")) {
					String newModeBody = (modeBody.equals("")) ? "-"+type : modeBody+",-"+type;
					modes.addAll(generateModesAux(currentAttribute+1, attributes, metadata, newModeBody, headMode));
				}
			}
		}
		
		return modes;
	}
}
