package castor.modetransform;


import castor.modetransform.transformmodel.Relation;
import castor.modetransform.transformmodel.TransformationSchema;
import castor.modetransform.transformmodel.TransformationTuple;

import org.apache.log4j.Logger;

import java.util.*;

public class ModeGeneratorHelper {

    final static Logger logger = Logger.getLogger(ModeGeneratorHelper.class);

    @SuppressWarnings("unchecked")
	public static void generateMode(TransformationSchema tschema, List<String> modes) {
        logger.info("------ Generating modes for target relations ---------");
        List<TransformationTuple> transformationTupleList = tschema.getMembers();
        for (TransformationTuple transformationTuple : transformationTupleList) {
            for (Relation targetRelation : transformationTuple.getTargetRelation()) {
                List<Set<String>> attributesTypeList = targetRelation.getAttributeTypes();
                Set<String>[] temp = new HashSet[attributesTypeList.size()];
                Set<String>[] attributesTypeSet = attributesTypeList.toArray(temp);
                List<List<String>> modeList = ModeGeneratorHelper.cartesianProduct(targetRelation.getName(), attributesTypeSet);
                String targetRelationName = targetRelation.getName();
                if (modeList == null) {
                    Set<String> attributeType = attributesTypeSet[0];
                    for (Object attributeObject : attributeType) {
                        logger.info(targetRelationName.toLowerCase() + "(" + attributeObject + ")");
                        modes.add(targetRelationName.toLowerCase() + "(" + attributeObject + ")");
                    }
                } else {
                    for (List<String> modeString : modeList) {
                        if (isModeValid(modeString)) {
                            Collections.reverse(modeString);
                            String finalModeString = modeString.toString().substring(1, modeString.toString().length() - 1);
                            finalModeString = finalModeString.replaceAll("\\s", "");
                            logger.info(targetRelationName.toLowerCase() + "(" + finalModeString + ")");
                            modes.add(targetRelationName.toLowerCase() + "(" + finalModeString + ")");
                        }
                    }
                }
            }
        }
    }


    public static boolean isModeValid(List<String> modeList) {
        int plus = 0;
        for (String modeString : modeList) {
            if (modeString.startsWith("+"))
                plus++;
        }
        if (plus == 0 || plus > 1)
            return false;
        return true;
    }

    @SafeVarargs
	public static List<List<String>> cartesianProduct(String k, Set<String>... sets) {
        List<List<String>> retset = null;
        if (sets.length >= 2)
            retset = _cartesianProduct(0, sets);
        return retset;
    }

    @SafeVarargs
	public static List<List<String>> _cartesianProduct(int index, Set<String>... sets) {
        List<List<String>> ret = new ArrayList<List<String>>();
        if (index == sets.length) {
            ret.add(new ArrayList<String>());
        } else {
            for (String obj : sets[index]) {
                for (List<String> set : _cartesianProduct(index + 1, sets)) {
                    set.add(obj);
                    ret.add(set);
                }
            }
        }
        return ret;
    }
}
