package castor.clients;

import castor.ddlindextract.main.DDLIndMain;
import castor.modetransform.main.TransformMain;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by Sudhanshu on 06/10/17.
 * preprocessInput can be used for two tasks:
 * 1) DDLIndExtraction : Extracts IND from DDL and also outputs invalid pks
 * parameters required: ddl
 * 2) ModeTransformation: For given input modes and transformation schema -> generate modes for transformed schema
 * parameters required:  dataModelFile, transformSchema
 */
public class CastorInputClient {
    @Option(name = "-ddl", usage = "Input file location", required = false)
    private String ddl;

    @Option(name = "-dataModelFile", usage = "Input file location", required = false)
    private String dataModelFile;

    @Option(name = "-transformSchema", usage = "Output file location", required = false)
    private String transformSchema;


    final static Logger logger = Logger.getLogger(CastorInputClient.class);


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CastorInputClient client = new CastorInputClient();
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
        String indResult = null;
        String modeResult = null;
        if (ddl != null) {
            DDLIndMain ddlIndMain = new DDLIndMain();
            indResult = ddlIndMain.extractIndFromDDL(ddl);
            if (indResult.equals("Failed")) {
                logger.error("Error while extracting inds from ddl ...");
            }
        }

        if (dataModelFile != null && transformSchema != null) {
            TransformMain transformMain = new TransformMain();
            modeResult = transformMain.generateModesUsingTranformation(dataModelFile, transformSchema);
            if (modeResult.equals("Failed")) {
                logger.error("Error while generating modes from transformation ...");
            }
        }
    }
}
