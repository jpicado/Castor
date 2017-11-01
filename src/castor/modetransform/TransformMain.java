package castor.modetransform;

import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.utils.Constants;
import castor.utils.FileUtils;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class TransformMain {


    final static Logger logger = Logger.getLogger(TransformMain.class);

    public static boolean generateModesUsingTransformation(String dataModelFile, String transformSchema, String outputFile) {
        logger.info("Running ModeTransformation ");
        // Get input mode information
        JsonObject dataModelJson = FileUtils.convertFileToJSON(dataModelFile);
        DataModel dataModel = readDataModelFromJson(dataModelJson);
        LinkedHashSet<String> modeSet = new LinkedHashSet<>();
        Map<String, List<LinkedHashMap<String,String>>> relationMap = new HashMap<>();
        // Get input transformation information
        readTransformationSchema(modeSet, transformSchema, dataModel, relationMap);
        List<String> modes = new ArrayList<String>(modeSet);
        Boolean result = FileUtils.writeModeToJsonFormat(null, dataModel.getModeH().toString(), modes, dataModel.getSpName(), outputFile);
        return result;
    }

    /*
     * Read data model from JSON object
     */
    private static DataModel readDataModelFromJson(JsonObject dataModelJson) {
        DataModel dataModel;
        try {
            logger.info("Reading mode file...");
            dataModel = JsonSettingsReader.readDataModelForTransformation(dataModelJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataModel;
    }

    /*
     * Read each entry from transformation file 
     */
    private static void readTransformationSchema(LinkedHashSet<String> modes, String transformFile, DataModel dataModel, Map<String, List<LinkedHashMap<String,String>>> relationMap) {
        Stream<String> stream = null;
        try {
            logger.info("Reading Transformation file...");
            stream = Files.lines(Paths.get(transformFile));
            {
                stream.filter(s -> !s.isEmpty()).forEach(s -> processTransformationSchema(modes, s, dataModel, relationMap));
            }
        } catch (IOException e) {
            logger.error("Error while processing tranformation file");
            e.printStackTrace();
        }
        stream.close();
    }

    /*
     * Call methods which perform transformation logic for each transformation entry.
     * First join all the modes from the source relations and using this resultSet generate target relation modes
     */
    private static void processTransformationSchema(LinkedHashSet<String> modes, String lineString, DataModel dataModel, Map<String, List<LinkedHashMap<String,String>>> relationMap) {
        lineString = lineString.replaceAll("\\s", "").toLowerCase();
        String[] line = lineString.split(Constants.TransformDelimeter.ARROW.getValue());
        List<LinkedHashMap<String,String>> result = null;
        try {
            List<String> sourceRelationList = createSourceRelationModeList(line[0], dataModel, relationMap);
            for (int i = 0; i < sourceRelationList.size(); i++) {
                result = joinSourceRelationModes(result, relationMap.get(sourceRelationList.get(i)));
            }
            createTargetRelationModes(line[1], result, modes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Joins the source relation modes
     */
    public static List<LinkedHashMap<String,String>> joinSourceRelationModes(List<LinkedHashMap<String,String>> modesList1, List<LinkedHashMap<String,String>> modesList2) {
        if (modesList1 == null)
            return modesList2;

        //Find the valid joins and add them to resultList. Invalid joins are those having more than one +attributes
        List<LinkedHashMap<String,String>> joinResult = new ArrayList<>();
        List<LinkedHashMap<String,String>> invalidPlusJoins = new ArrayList<>();
        for (LinkedHashMap<String, String> sourceRelation1 : modesList1) {
            for (LinkedHashMap<String, String> sourceRelation2 : modesList2) {
                LinkedHashMap<String, String> tempList = null;
                tempList = new LinkedHashMap<>();
                tempList.putAll(sourceRelation2);
                tempList.putAll(sourceRelation1);

                int plusCount = 0;
                boolean invalidJoin = false;
                for (Map.Entry<String, String> map : tempList.entrySet()) {
                    if (map.getValue().startsWith(Constants.ModeType.INPUT.getValue())) {
                        plusCount++;
                    }
                    if (plusCount > 1) {
                        if (!invalidPlusJoins.contains(sourceRelation2))
                            invalidPlusJoins.add(sourceRelation2);
                        invalidJoin = true;
                    }
                }
                if (!invalidJoin) {
                    joinResult.add(tempList);
                }
            }
        }

        //Run another For loop to handle Invalid joins
        for (LinkedHashMap<String, String> sourceRelation1 : invalidPlusJoins) {
            for (LinkedHashMap<String, String> sourceRelation2 : modesList1) {
                LinkedHashMap<String, String> tempList = null;
                tempList = new LinkedHashMap<>();
                tempList.putAll(sourceRelation2);
                tempList.putAll(sourceRelation1);

                int plusCount = 0;
                boolean invalidJoin = false;
                for (Map.Entry<String, String> map : tempList.entrySet()) {
                    if (map.getValue().startsWith(Constants.ModeType.INPUT.getValue())) {
                        plusCount++;
                    }
                    if (plusCount > 1) {
                        invalidJoin = true;
                    }
                }
                if (!invalidJoin) {
                    joinResult.add(tempList);
                }
            }
        }
        return joinResult;
    }

    /*
     * Creates modelist for all the source relations
     */
    private static List<String> createSourceRelationModeList(String sourceRelationString, DataModel dataModel, Map<String, List<LinkedHashMap<String,String>>> relationMap) {
        logger.info("Creating source relation Modelist for :: " + sourceRelationString);
        String[] relations = sourceRelationString.split(Constants.TransformDelimeter.SLASH_CLOSE_PARA.getValue());
        List<String> sourceRelationList = new ArrayList<>();
        for (String relation : relations) {
            String relationName = relation.substring(0, relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
            if (relationName.startsWith(Constants.TransformDelimeter.COMMA.getValue())) {
                relationName = relation.substring(1, relationName.length());
            }
            List<LinkedHashMap<String,String>> relationModeList = null;
            if (relationMap.containsKey(relation)) {
                relationModeList = relationMap.get(relation);
            } else {
                relationModeList = new ArrayList<>();
                relationMap.put(relationName, relationModeList);
            }

            String attributes = (String) relation.subSequence(relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, relation.length());
            String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());
            List<List<String>> modesList = dataModel.getModesBMap().get(relationName);

            for (List<String> mode : modesList) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                int count = 0;
                for (String str : mode) {
                    map.put(attributeArray[count++], str);
                }
                relationModeList.add(map);
            }
            sourceRelationList.add(relationName);
        }
        return sourceRelationList;
    }

    /*
     * Generate modes for the target relations 
     */
    private static void createTargetRelationModes(String targetRelationString, List<LinkedHashMap<String,String>> joinResults, LinkedHashSet<String> modes) throws Exception {
        logger.info("Generating target relation Modes for :: " + targetRelationString);
        String[] relations = targetRelationString.split(Constants.TransformDelimeter.SLASH_CLOSE_PARA.getValue());
        for (String relation : relations) {
            String relationName = relation.substring(0, relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
            if (relationName.startsWith(Constants.TransformDelimeter.COMMA.getValue())) {
                relationName = relation.substring(1, relationName.length());
            }

            String attributes = (String) relation.subSequence(relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, relation.length());
            String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());

            List<String> modeString = null;
            for (LinkedHashMap<String,String> joinList : joinResults) {
                modeString = new ArrayList<>();
                for (String attribute : attributeArray) {
                    modeString.add(getAttributeValue(joinList, attribute));
                }
                String finalModeString = modeString.toString().substring(1, modeString.toString().length() - 1);
                finalModeString = finalModeString.replaceAll("\\s", "");
                int numberOfPositive = StringUtils.countMatches(finalModeString, Constants.ModeType.INPUT.getValue());
                if (numberOfPositive != 0) {
                    logger.info(relationName.toLowerCase() + Constants.TransformDelimeter.OPEN_PARA.getValue() + finalModeString + Constants.TransformDelimeter.CLOSE_PARA.getValue());
                    modes.add(relationName.toLowerCase() + Constants.TransformDelimeter.OPEN_PARA.getValue() + finalModeString + Constants.TransformDelimeter.CLOSE_PARA.getValue());
                }
            }
        }
    }

    public static String getAttributeValue(LinkedHashMap<String, String> joinList, String attribute) {
        if (joinList.containsKey(attribute))
            return joinList.get(attribute).toString();
        return null;

    }
}