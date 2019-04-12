package castor.language;

import java.util.List;
import java.util.Map;

import castor.utils.Pair;

public class Schema {

	private String name;
	private Map<String, Relation> relations;
	private Map<String, List<InclusionDependency>> inclusionDependencies;
	private Map<Pair<String,Integer>, List<MatchingDependency>> matchingDependencies;
	
	public Schema(Map<String, Relation> relations) {
		super();
		this.relations = relations;
	}
	
	public Schema(String name, Map<String, Relation> relations, Map<String, List<InclusionDependency>> inclusionDependencies) {
		super();
		this.name = name;
		this.relations = relations;
		this.inclusionDependencies = inclusionDependencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Relation> getRelations() {
		return relations;
	}
	
	public void setInclusionDependencies(Map<String, List<InclusionDependency>> inclusionDependencies){
		this.inclusionDependencies = inclusionDependencies;
	}
	
	public Map<String, List<InclusionDependency>> getInclusionDependencies(){
		return inclusionDependencies;
	}

	public Map<Pair<String, Integer>, List<MatchingDependency>> getMatchingDependencies() {
		return matchingDependencies;
	}

	public void setMatchingDependencies(Map<Pair<String, Integer>, List<MatchingDependency>> matchingDependencies) {
		this.matchingDependencies = matchingDependencies;
	}

}
