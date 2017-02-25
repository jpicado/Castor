package castor.profiling;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import castor.db.dataaccess.DAOFactory;
import castor.db.dataaccess.GenericDAO;
import castor.language.Relation;
import castor.language.Schema;
import castor.settings.JsonSettingsReader;
import castor.utils.FileUtils;
import castor.utils.TimeWatch;

import com.google.gson.JsonObject;

public class ApproximateINDDiscovery {

	@Option(name="-schema",usage="Schema file",required=true)
    private String schemaFile;
	
	@Argument
    private List<String> arguments = new ArrayList<String>();
	
	public static void main(String[] args) {
		ApproximateINDDiscovery discovery = new ApproximateINDDiscovery();
		discovery.discoverApproximateINDs(args);
	}
	
	public void discoverApproximateINDs(String[] args) {
		TimeWatch tw = TimeWatch.start();
		
		// Parse the arguments
        try {
        	CmdLineParser parser = new CmdLineParser(this);
			parser.parseArgument(args);
        } catch (CmdLineException e) {
			System.err.println(e.getMessage());
			return;
		}
		
		// Read JSON object
        JsonObject schemaJson = FileUtils.convertFileToJSON(schemaFile);
        
        // Read schema
        Schema schema;
        try {
			schema = JsonSettingsReader.readSchema(schemaJson);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	// Create data access objects and set URL of data
        	String dbUrl = "localhost";
        	try {
        		daoFactory.initConnection(dbUrl);
        	}
        	catch (RuntimeException e) {
        		System.err.println("Unable to connect to server with URL: " + dbUrl);
        		return;
        	}
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
    		
    		Set<String> relations = relationsToConsider();
    		
    		String numeratorQueryTemplate = "select count(distinct(r.{1})) from (select {1} from {0}) r, (select {3} from {2}) s where r.{1} = s.{3};";
    		String denominatorQueryTemplate = "select count(distinct({1})) from {0};";
    		for (Relation relation1 : schema.getRelations().values()) {
//    			if (!relations.contains(relation1.getName()))
//    				continue;
    				
    		    for (String attribute1 : relation1.getAttributeNames()) {
    		    	
    		    	for (Relation relation2 : schema.getRelations().values()) {
//    		    		if (!relations.contains(relation2.getName()))
//    	    				continue;
    		    		
    		    		for (String attribute2 : relation2.getAttributeNames()) {
    		    			
    		    			// If same relation and attribute, continue
    		    			if (relation1.getName().equals(relation2.getName()) && attribute1.equals(attribute2))
    		    				continue;
    		    			
    		    			String numeratorQuery = MessageFormat.format(numeratorQueryTemplate, relation1.getName(), attribute1, relation2.getName(), attribute2);
    		        		String denominatorQuery = MessageFormat.format(denominatorQueryTemplate, relation1.getName(), attribute1);
    		        		
    		        		long numerator = genericDAO.executeScalarQuery(numeratorQuery);
    		        		long denominator = genericDAO.executeScalarQuery(denominatorQuery);
    		        		
    		        		if (denominator == 0) {
    		        			continue;
    		        		}
    		        		
    		        		double error = 1.0 - ((double)numerator/(double)denominator);
    		        		
    		        		if (error <= 0.5)
    		        			System.out.println(relation1.getName()+"["+attribute1+"] < "+ relation2.getName()+"["+attribute2+"] - error: "+error);
    		    			
//    		    			System.out.println(relation1.getName()+"."+attribute1+" - "+relation2.getName()+"."+attribute2);
    		    		}
    		    	}
    		    }
    		}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	// Close connection to DBMS
        	daoFactory.closeConnection();
        }
        
        System.out.println("Finished in: "+tw.time()+"ms");
	}
	
	private Set<String> relationsToConsider() {
		Set<String> relations = new HashSet<String>();
		
		relations.add("student");
		relations.add("inphase");
		relations.add("yearsinprogram");
		relations.add("professor");
		relations.add("hasposition");
		relations.add("courselevel");
		relations.add("taughtby");
		relations.add("ta");
		relations.add("publication");
		
//		relations.add("advisedby_fold1_train_neg");
		relations.add("advisedby_fold1_train_pos");
		
		return relations;
	}
}
