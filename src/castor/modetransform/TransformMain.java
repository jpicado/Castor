package castor.modetransform;

import castor.modetransform.ModeGeneratorHelper;
import castor.modetransform.transformmodel.Relation;
import castor.modetransform.transformmodel.TransformationSchema;
import castor.modetransform.transformmodel.TransformationTuple;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.utils.Constants;
import castor.utils.FileUtils;

import com.google.gson.JsonObject;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class TransformMain {


    final static Logger logger = Logger.getLogger(TransformMain.class);
    private DataModel dataModel;
    private TransformationSchema tScehma;
    private List<String> modes;


    public Boolean generateModesUsingTranformation(String dataModelFile, String transformSchema, String outputFile) {
        logger.info("Running ModeTransformation ");
        // Get input mode information
        JsonObject dataModelJson = FileUtils.convertFileToJSON(dataModelFile);
        dataModel = this.readDataModelFromJson(dataModelJson);
        tScehma = new TransformationSchema();
        modes = new ArrayList<String>();
        // Get input transformation information
        this.readTransformationSchema(transformSchema);
        ModeGeneratorHelper.generateMode(tScehma, modes);
        Boolean result = FileUtils.writeModeToJsonFormat(null, dataModel.getModeH().toString(), modes, dataModel.getSpName(), outputFile);
        return result;
    }


    /*
     * Read data model from JSON object
     */
    private DataModel readDataModelFromJson(JsonObject dataModelJson) {
        DataModel dataModel;
        try {
            logger.info("Reading mode file...");
            dataModel = JsonSettingsReader.readDataModelForTransformation(dataModelJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataModel;
    }

    private void readTransformationSchema(String transformFile) {
        Stream<String> stream = null;
        try {
            logger.info("Reading Transformation file...");
            stream = Files.lines(Paths.get(transformFile));
            {
                stream.filter(s -> !s.isEmpty()).forEach(s -> processTransformationSchema(s));
            }
        } catch (IOException e) {
            logger.error("Error while processing tranformation file");
            e.printStackTrace();
        }
        stream.close();
    }

    private void processTransformationSchema(String lineString) {
        lineString = lineString.replaceAll("\\s", "").toLowerCase();
        String[] line = lineString.split(Constants.TransformDelimeter.ARROW.getValue());
        TransformationTuple transformationTuple = null;
        try {
            List<Relation> sourceRelation = createSourceRelationObject(line[0]);
            List<Relation> targetRelation = createTargetRelationObject(line[1], sourceRelation);
            transformationTuple = new TransformationTuple();
            transformationTuple.setSourceRelation(sourceRelation);
            transformationTuple.setTargetRelation(targetRelation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.tScehma.addToMembersList(transformationTuple);
    }

    public List<Relation> createSourceRelationObject(String sourceRelationString) {
        logger.info("Creating source relation objects using :: " + sourceRelationString);
        List<Relation> sourceRelationList = new ArrayList<Relation>();
        String[] relations = sourceRelationString.split(Constants.TransformDelimeter.SLASH_CLOSE_PARA.getValue());
        for (String relation : relations) {
            String relationName = relation.substring(0, relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
            if (relationName.startsWith(Constants.TransformDelimeter.COMMA.getValue())) {
                relationName = relation.substring(1, relationName.length());
            }
            Relation sourceRelation = new Relation(relationName);
            String attributes = (String) relation.subSequence(relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, relation.length());
            String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());
            List<Set<String>> modeTypes = this.dataModel.getModesBMap().get(relationName);
            List<Map<String, Set<String>>> sourceRelationAttributes = new ArrayList<Map<String, Set<String>>>();
            List<Set<String>> attributeTypes = new ArrayList<Set<String>>();
            int count = 0;
            for (String attribute : attributeArray) {
                Map<String, Set<String>> map = new HashMap<String, Set<String>>();
                map.put(attribute, modeTypes.get(count));
                sourceRelationAttributes.add(map);
                attributeTypes.add(modeTypes.get(count));
                count++;
            }
            sourceRelation.setAttributeTypes(attributeTypes);
            sourceRelation.setAttributes(sourceRelationAttributes);
            sourceRelationList.add(sourceRelation);
        }
        return sourceRelationList;
    }


    public List<Relation> createTargetRelationObject(String targetRelationString, List<Relation> sourceRelation) throws Exception {
        logger.info("Creating target relation objects using :: " + targetRelationString);
        List<Relation> targetRelationList = new ArrayList<Relation>();
        String[] relations = targetRelationString.split(Constants.TransformDelimeter.SLASH_CLOSE_PARA.getValue());
        for (String relation : relations) {
            String relationName = relation.substring(0, relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()));
            if (relationName.startsWith(Constants.TransformDelimeter.COMMA.getValue())) {
                relationName = relation.substring(1, relationName.length());
            }
            String attributes = (String) relation.subSequence(relation.indexOf(Constants.TransformDelimeter.OPEN_PARA.getValue()) + 1, relation.length());
            Relation targetRelation = new Relation(relationName);
            List<Map<String, Set<String>>> targetAttributesList = new ArrayList<Map<String, Set<String>>>();
            List<Set<String>> attributeTypes = new ArrayList<Set<String>>();
            String[] attributeArray = attributes.split(Constants.TransformDelimeter.COMMA.getValue());
            Set<String> attributeType = null;
            for (String attribute : attributeArray) {
                outerloop:
                for (Relation sourceRel : sourceRelation) {
                    for (Map<String, Set<String>> map : sourceRel.getAttributes()) {
                        if (map.containsKey(attribute)) {
                            attributeType = (Set<String>) map.get(attribute);
                            break outerloop;
                        }
                    }
                }
                if (attributeType != null) {
                    Map<String, Set<String>> map = new HashMap<String, Set<String>>();
                    map.put(attribute, (Set<String>) attributeType);
                    targetAttributesList.add(map);
                    attributeTypes.add((Set<String>) attributeType);
                } else {
                    throw new Exception("Attribute from source relation not found in target");
                }
            }
            //Update attributes types with +/- modes
            attributeTypes = updateAttributeTypes(attributeTypes);
            targetRelation.setAttributeTypes(attributeTypes);
            targetRelation.setAttributes(targetAttributesList);
            targetRelationList.add(targetRelation);
        }
        return targetRelationList;
    }


    public List<Set<String>> updateAttributeTypes(List<Set<String>> typeSet) {
        List<Set<String>> typeSetList = new ArrayList<Set<String>>();
        for (Set<String> set : typeSet) {
            Set<String> temp = new HashSet<String>();
            for (String str : set) {
                if (!str.startsWith(Constants.ModeType.CONSTANT.getValue())) {
                    temp.add(Constants.ModeType.INPUT.getValue() + str);
                    if (typeSet.size() > 1) {
                        temp.add(Constants.ModeType.OUTPUT.getValue() + str);
                    }
                } else {
                    temp.add(str);
                }
            }
            typeSetList.add(temp);
        }
        return typeSetList;
    }

}
