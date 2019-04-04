package castor.profiling;

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
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApproximateIndSourceToTargetDiscovery {
    @Option(name="-schema",usage="Schema file",required=true)
    private String schemaFile;

    @Option(name="-maxerror",usage="Maximum error",required=true)
    private double maxError;

    @Option(name="-outfile",usage="Output file",required=true)
    private String outfile;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) {
        ApproximateIndSourceToTargetDiscovery discovery = new ApproximateIndSourceToTargetDiscovery();
        discovery.discoverApproximateINDsSourceToTarget(args);
    }


    public void discoverApproximateINDsSourceToTarget(String[] args) {
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
            schema = JsonSettingsReader.readSchemaV1(schemaJson);
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

            Relation sourceRelation = schema.getTarget();

            for (String attribute1 : sourceRelation.getAttributeNames()) {

                for (Relation targetRelation : schema.getRelations().values()) {
                    for (String attribute2 : targetRelation.getAttributeNames()) {

                        String leftRelationQuery = MessageFormat.format(queryTemplate, sourceRelation.getName(), attribute1);
                        String rightRelationQuery = MessageFormat.format(queryTemplate, targetRelation.getName(), attribute2);

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

                        if (error <= maxError) {
                            //System.out.println(relation1.getName()+"["+attribute1+"] < "+ relation2.getName()+"["+attribute2+"] - error: "+error);
                            inds.add(("("+sourceRelation.getName()+"."+attribute1+") < ("+ targetRelation.getName()+"."+attribute2+") < "+error).toLowerCase());
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
