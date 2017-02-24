package castor.profiling;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        
        Relation student = schema.getRelations().get("student");
        Relation inphase = schema.getRelations().get("inphase");
		
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
//    		long numerator = genericDAO.executeScalarQuery("select count(distinct(s.student)) from (select student from student) s, (select student from inPhase) p where s.student=p.student;");
//    		long denominator = genericDAO.executeScalarQuery("select count(distinct(student)) from student;");
//    		double error = 1.0 - (double)(numerator/denominator);
//    		System.out.println(error);
    		
//        	String query;
//        	ClientResponse response;
//        	VoltTable table;
//        	
//    		query = "select count(distinct(s.student)) from (select student from student) s, (select student from inPhase) p where s.student=p.student;";
//    		response = VoltDBConnectionContainer.getInstance().getClient().callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
//			table = response.getResults()[0];
//			table.advanceRow();
//			long numerator = table.getLong(0);
//    		
//    		query = "select count(distinct(student)) from student;";
//    		response = VoltDBConnectionContainer.getInstance().getClient().callProcedure(VoltDBGenericDAO.ADHOC_QUERY, query);
//			table = response.getResults()[0];
//			table.advanceRow();
//			long denominator = table.getLong(0);
//			
//			double error = 1.0 - (double)(numerator/denominator);
//    		System.out.println(error);
    		
    		
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
    		        		
    		        		if (error <= 0.5)
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
	}
}
