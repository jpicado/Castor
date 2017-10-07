package castor.modetransform.transformmodel;

import java.util.ArrayList;
import java.util.List;

public class TransformationTuple {
    private List<Relation> sourceRelation;
    private List<Relation> targetRelation;

    public List<Relation> getTargetRelation() {
        return targetRelation;
    }

    public void setTargetRelation(List<Relation> targetRelation) {
        this.targetRelation = targetRelation;
    }

    public void addTargetRelation(Relation relation) {
        if (targetRelation == null) {
            targetRelation = new ArrayList<Relation>();
        }
        this.targetRelation.add(relation);
    }

    public List<Relation> getSourceRelation() {
        return sourceRelation;
    }

    public void setSourceRelation(List<Relation> sourceRelation) {
        this.sourceRelation = sourceRelation;
    }

    public void addSourceRelation(Relation relation) {
        if (sourceRelation == null) {
            sourceRelation = new ArrayList<Relation>();
        }
        this.sourceRelation.add(relation);
    }

}
