package castor.inputdecoders;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import castor.language.Argument;
import castor.language.Determination;
import castor.language.IdentifierType;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;

public class KinshipDecoder implements InputDecoder {

	@Override
	public void readSchemaFileFromXml(String path){
		
	}
	
	@Override
	public boolean readSchemaFileFromSql(String path){
		return false;
	}
	
	@Override
	public boolean readUserInputFile(String filePath){
		return false;
	}
	
	@Override
	public Mode getModeH() {
		// TODO Read from file
		List<Argument> arguments = new LinkedList<Argument>();
		arguments.add(new Argument("person", IdentifierType.INPUT));
		arguments.add(new Argument("person", IdentifierType.INPUT));
		return new Mode("father", arguments);
	}

	@Override
	public List<Mode> getModesB() {
		// TODO Read from file
		List<Argument> motherArguments = new LinkedList<Argument>();
		motherArguments.add(new Argument("person", IdentifierType.INPUT));
		motherArguments.add(new Argument("person", IdentifierType.INPUT));
		
		List<Argument> husbandArguments = new LinkedList<Argument>();
		husbandArguments.add(new Argument("person", IdentifierType.INPUT));
		husbandArguments.add(new Argument("person", IdentifierType.INPUT));
		
		List<Argument> sonArguments = new LinkedList<Argument>();
		sonArguments.add(new Argument("person", IdentifierType.INPUT));
		sonArguments.add(new Argument("person", IdentifierType.INPUT));
		
		List<Mode> modes = new LinkedList<Mode>();
		modes.add(new Mode("mother", motherArguments));
		modes.add(new Mode("husband", husbandArguments));
		modes.add(new Mode("son", sonArguments));
		
		return modes;
	}

	@Override
	public List<Determination> getDeterminations() {
		// TODO Read from file
		List<Determination> determinations = new LinkedList<Determination>();
		determinations.add(new Determination("mother", 2));
		determinations.add(new Determination("husband", 2));
		determinations.add(new Determination("son", 2));
		return determinations;
	}

	@Override
	public String getSPNameTemplate() {
		// TODO Read from file
		return "KinshipTestProcedure";
	}

	@Override
	public int getIterations() {
		// TODO Read from file
		return 1;
	}
	
	@Override
	public int getRecall() {
		// TODO Read from file
		return 10;
	}
	
	@Override
	public int getMaxTerms() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getServerName() {
		// TODO Read from file
		return "localhost";
	}

	@Override
	public String getTemporaryTableName() {
		// TODO Read from file
		return "temp";
	}

	@Override
	public Schema getSchema() {
		// TODO Read from file
		List<String> fatherAttributes = new LinkedList<String>();
		fatherAttributes.add("father");
		fatherAttributes.add("child");
		
		List<String> motherAttributes = new LinkedList<String>();
		motherAttributes.add("mother");
		motherAttributes.add("child");
		
		List<String> husbandAttributes = new LinkedList<String>();
		husbandAttributes.add("husband");
		husbandAttributes.add("wife");
		
		List<String> sonAttributes = new LinkedList<String>();
		sonAttributes.add("son");
		sonAttributes.add("parent");
		
		Map<String, Relation> relations = new HashMap<String, Relation>();
		relations.put("father", new Relation("father", fatherAttributes));
		relations.put("mother", new Relation("mother", motherAttributes));
		relations.put("husband", new Relation("husband", husbandAttributes));
		relations.put("son", new Relation("son", sonAttributes));
		
		return new Schema(relations);
	}

	@Override
	public Map<String, List<InclusionDependency>> getInclusionDep() {
		// TODO Auto-generated method stub
		return null;
	}

}
