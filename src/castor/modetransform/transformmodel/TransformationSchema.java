package castor.modetransform.transformmodel;

import java.util.ArrayList;
import java.util.List;

public class TransformationSchema {
    private List<TransformationTuple> members;

    public List<TransformationTuple> getMembers() {
        return members;
    }

    public void setMembers(List<TransformationTuple> members) {
        this.members = members;
    }

    public void addToMembersList(TransformationTuple transformationTuple) {
        if (members == null) {
            members = new ArrayList<TransformationTuple>();
        }
        members.add(transformationTuple);
    }
}
