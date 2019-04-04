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

import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.JsonSettingsReader;
import castor.utils.FileUtils;
import castor.utils.TimeWatch;

import com.google.gson.JsonObject;

public class ApproximateINDDiscovery {

	@Option(name="-schema",usage="Schema file",required=true)
    private String schemaFile;
	
	@Option(name="-maxerror",usage="Maximum error",required=true)
    private double maxError;

	@Option(name="-outfile",usage="Output file",required=true)
	private String outfile;

	@Argument
    private List<String> arguments = new ArrayList<String>();
	
	public static void main(String[] args) {
		ApproximateINDDiscovery discovery = new ApproximateINDDiscovery();
		discovery.discoverApproximateINDsV2(args);
	}
	
	/*
	 * Discovers and prints approximate INDs
	 * This version finds overlap between two relations by joining relations in DB
	 */
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
    		
    		String numeratorQueryTemplate = "select count(distinct(r.{1})) from (select {1} from {0}) r, (select {3} from {2}) s where r.{1} = s.{3};";
    		String denominatorQueryTemplate = "select count(distinct({1})) from {0};";
    		for (Relation relation1 : schema.getRelations().values()) {
    		    for (String attribute1 : relation1.getAttributeNames()) {
    		    	
    		    	for (Relation relation2 : schema.getRelations().values()) {
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
    		        		
    		        		if (error <= maxError)
    		        			System.out.println(relation1.getName()+"["+attribute1+"] < "+ relation2.getName()+"["+attribute2+"] - error: "+error);
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
        
        System.out.println("Finished in: "+tw.time()+" ms");
	}
	
	/*
	 * Discovers and prints approximate INDs
	 * This version finds overlap between two relations by loading them to memory and computing overlap programatically
	 */
	public void discoverApproximateINDsV2(String[] args) {
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
			//Create list to store all the inds
			List<String> inds = new ArrayList<>();

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

    		String queryTemplate = "select distinct({1}) from {0};";
    		for (Relation relation1 : schema.getRelations().values()) {
    		    for (String attribute1 : relation1.getAttributeNames()) {
    		    	
    		    	for (Relation relation2 : schema.getRelations().values()) {
    		    		for (String attribute2 : relation2.getAttributeNames()) {
    		    			
    		    			// If same relation and attribute, continue
    		    			if (relation1.getName().equals(relation2.getName()) && attribute1.equals(attribute2))
    		    				continue;
    		    			
    		    			String leftRelationQuery = MessageFormat.format(queryTemplate, relation1.getName(), attribute1);
    		    			String rightRelationQuery = MessageFormat.format(queryTemplate, relation2.getName(), attribute2);
    		    			
    		    			GenericTableObject leftResult = genericDAO.executeQuery(leftRelationQuery);
    		    			int leftAttributeCount = leftResult.getTable().size();
    		    			
    		    			// If denominator is 0, skip
    		    			if (leftAttributeCount == 0) {
    		        			continue;
    		        		}
    		    			
    		    			GenericTableObject rightResult = genericDAO.executeQuery(rightRelationQuery);
    		    			
    		    			Set<Tuple> leftRelationValues = new HashSet<Tuple>(leftResult.getTable());
    		    			Set<Tuple> rightRelationValues = new HashSet<Tuple>(rightResult.getTable());
    		    			
    		    			int intersectionCount = 0;
    		    			
    		    			for (Tuple tuple : leftRelationValues) {
								if (rightRelationValues.contains(tuple))
									intersectionCount++;
							}
    		    			
    		        		double error = 1.0 - ((double)intersectionCount/(double)leftAttributeCount);
    		        		
    		        		if (error <= maxError)
    		        			//System.out.println(relation1.getName()+"["+attribute1+"] < "+ relation2.getName()+"["+attribute2+"] - error: "+error);
								inds.add(("("+relation1.getName()+"."+attribute1+") < ("+ relation2.getName()+"."+attribute2+") < "+error).toLowerCase());
    		    		}
    		    	}
    		    }
    		}
			FileUtils.writeToFile(outfile, inds);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	// Close connection to DBMS
        	daoFactory.closeConnection();
        }
        
        System.out.println("Finished in: "+tw.time()+" ms");
	}
}
