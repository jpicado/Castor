package castor.clients;

import castor.ddlindextract.DDLIndMain;
import castor.modetransform.TransformMain;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

/**
 * Created by Sudhanshu on 06/10/17.
 * preprocessInput can be used for two tasks:
 * 1) DDLIndExtraction : Extracts IND from DDL and also outputs invalid pks
 * parameters required: ddl
 * 2) ModeTransformation: For given input modes and transformation schema -> generate modes for transformed schema
 * parameters required:  dataModelFile, transformSchema
 */
public class PreprocessClient {
    @Option(name = "-ddl", usage = "Input file location", required = false, handler=StringArrayOptionHandler.class)
    private String[] ddlPath;
    
    @Option(name = "-outputIndFile", usage = "Output file location", required = false, handler=StringArrayOptionHandler.class)
    private String[] outputIndFilePath;

    @Option(name = "-dataModelFile", usage = "Input file location", required = false, handler=StringArrayOptionHandler.class)
    private String[] dataModelFilePath;

    @Option(name = "-transformSchema", usage = "Output file location", required = false, handler=StringArrayOptionHandler.class)
    private String[] transformSchemaPath;
    
    @Option(name = "-outputModesFile", usage = "Output file location", required = false, handler=StringArrayOptionHandler.class)
    private String[] outputModesFilePath;


    final static Logger logger = Logger.getLogger(PreprocessClient.class);

    public static void main(String[] args) {
        PreprocessClient client = new PreprocessClient();
        client.preprocessInput(args);
    }

    public void preprocessInput(String[] args) {
        // Parse the arguments
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            logger.error(e.getMessage());
            parser.printUsage(System.out);
            return;
        }

        logger.info("Preprocessing input ... ");
        Boolean indResult;
        Boolean modeResult;
        if (ddlPath != null && outputIndFilePath != null) {
        		String ddl = getOption(ddlPath);
            String outputIndFile = getOption(outputIndFilePath);
            indResult = DDLIndMain.extractIndFromDDL(ddl, outputIndFile);
            if (indResult==false) {
                logger.error("Error while extracting inds from ddl ...");
            }
        }
        
        if (dataModelFilePath != null && transformSchemaPath != null && outputModesFilePath != null) {
        		String dataModelFile = getOption(dataModelFilePath);
            String transformSchema = getOption(transformSchemaPath);
            String outputModesFile = getOption(outputModesFilePath);
            modeResult = TransformMain.generateModesUsingTranformation(dataModelFile, transformSchema, outputModesFile);
            if (modeResult==false) {
                logger.error("Error while generating modes from transformation ...");
            }
        }
    }
    
    /*
	 * Get an option string from multiple strings
	 */
	private String getOption(String[] option) {
	    StringBuilder cmd = new StringBuilder();
	    for (int i = 0; i < option.length; i++) {
	        cmd.append(option[i]);
	        cmd.append(" ");
	    }
	    return cmd.toString().trim().replaceAll("^\"|\"$", "");
	}
}
