package castor.modetransform.transformmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Relation {
    private String name;
    private List<Map<String, Set<String>>> attributes;
    private List<Set<String>> attributeTypes;

    public Relation(String name) {
        super();
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Set<String>>> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Map<String, Set<String>>> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(Map<String, Set<String>> value) {
        if (attributes == null) {
            attributes = new ArrayList<Map<String, Set<String>>>();
        }
        attributes.add(value);
    }

    public List<Set<String>> getAttributeTypes() {
        return attributeTypes;
    }

    public void setAttributeTypes(List<Set<String>> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }
}
