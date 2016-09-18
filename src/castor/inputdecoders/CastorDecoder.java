package castor.inputdecoders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

public class CastorDecoder implements InputDecoder {

	private Schema schema;
	private Mode modeH;
	private List<Mode> modeB;
	private List<Determination> determinations;
	
	// Default values
	private int iteration 				= 1;
	private int recall 					= 20;
	private int maxterms 				= Integer.MAX_VALUE;
	private String serverName 			= "localhost";
	private String SPNameTemplate 		= "sp";
	private String temparoryTableName 	= "temp";
	
	@Override
	public void readSchemaFileFromXml(String path){
	}
	
	@Override
	public boolean readSchemaFileFromSql(String path){
		boolean success = true;
		String tempLine = null;
		BufferedReader buffer = null;
		String relationName = "";
		Map<String, Relation> relations = new HashMap<String, Relation>();
		int max_buf_size = 2048;
		File tempFile = new File (path);
		
		try {
			buffer = new BufferedReader(new FileReader(tempFile), max_buf_size);
			while ((tempLine = buffer.readLine()) != null) {
				if (tempLine.contains("CREATE TABLE ")){
					relationName = tempLine.replace("CREATE TABLE ", "").replace(" ", "").replace("(", "").toLowerCase();
					relations.put(relationName, new Relation(relationName, new LinkedList<String>()));
				} else if (tempLine.equals("")){
				} else if (tempLine.contains(");")){
				} else if (tempLine.contains("CONSTRAINT")) {
					// ignore
				} else{
					relations.get(relationName).getAttributeNames().add(relations.get(relationName).getAttributeNames().size(), tempLine.trim().split(" ")[0].toLowerCase());
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		
		schema = new Schema(relations);
		return success;
	}
	
	@Override
	public boolean readUserInputFile(String inputPath){
		boolean success = true;
		
		String tempLine = null;
		BufferedReader buffer = null;
		
		String modeHStr = "";
		String modeBStr = "";
		String detStr = "";
		String incs = "";
		
		boolean isIter = false;
		boolean isRecall = false;
		boolean isMaxTerms = false;
		boolean isServer = false;
		boolean isSPName = false;
		boolean isTempTable = false;
		
		int max_buf_size = 2048;
		
		File tempFile = new File (inputPath);
		
		try {
			buffer = new BufferedReader(new FileReader(tempFile), max_buf_size);
			while ((tempLine = buffer.readLine()) != null) {
				if (tempLine.contains("<iteration")){
					isIter = true;
				} else if (isIter){
					tempLine = tempLine.replace(" ", "").replace("\t", "");
					iteration = Integer.parseInt(tempLine);
					isIter = false;
				} else if (tempLine.contains("<recall")){
					isRecall = true;
				} else if (isRecall){
					tempLine = tempLine.replace(" ", "").replace("\t", "");
					recall = Integer.parseInt(tempLine);
					isRecall = false;
				} else if (tempLine.contains("<maxterms")){
					isMaxTerms = true;
				} else if (isMaxTerms){
					tempLine = tempLine.replace(" ", "").replace("\t", "");
					maxterms = Integer.parseInt(tempLine);
					isMaxTerms = false;
				}
				else if (tempLine.contains("<server")){
					isServer = true;
				} else if (isServer){
					serverName = tempLine.replace(" ", "").replace("\t", "");
					isServer = false;
				} else if (tempLine.contains("<temparoryTableName")){
					isTempTable = true;
				} else if (isTempTable){
					temparoryTableName = tempLine.replace(" ", "").replace("\t", "");
					isTempTable = false;
				} else if (tempLine.contains("<SPNameTemplate")){
					isSPName = true;
				} else if (isSPName){
					SPNameTemplate = tempLine.replace(" ", "").replace("\t", "");
					isSPName = false;
				} else if (tempLine.contains(":- modeh")){
					modeHStr = tempLine.replace(":- ","").replace(" ", "").replace("\t", "").toLowerCase();
				} else if (tempLine.contains(":- modeb")){
					modeBStr += tempLine.replace(":- ","").replace(" ", "").replace("\t", "").toLowerCase();
				} else if (tempLine.contains(":- determination")){
					detStr += tempLine.replace(":- ", "").replace(" ", "").replace("\t", "").toLowerCase();
				} else if (tempLine.contains(":- ind")){
					incs += tempLine.replace(":- ", "").replace(" ", "").replace("\t", "").toLowerCase();
				}
			}
			
			modeH = extractModeH(modeHStr);
			modeB = extractModeB(modeBStr);
			determinations = extractDeterminations(detStr);
			schema.setInclusionDependencies(extractInd(incs));
			
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		} finally {
			
		}
		return success;
	}
	
	private Map<String, List<InclusionDependency>> extractInd(String incs){
		Map<String, List<InclusionDependency>> ind = new HashMap<String, List<InclusionDependency>>();
		
		if (!incs.isEmpty()) {
			incs = incs.replace("ind(", "").replace(")", "");
			String [] inds = incs.split("\\.");
			String first = "";
			String second = "";
			for(int i = 0; i < inds.length; i++){
				first = inds[i].split(",")[0];
				second = inds[i].split(",")[1];
				if(!ind.containsKey(first.split(":")[0])){
					ind.put(first.split(":")[0], new ArrayList<InclusionDependency>());
				}
				ind.get(first.split(":")[0]).add(new InclusionDependency(first.split(":")[0], Integer.parseInt(first.split(":")[1]),
						second.split(":")[0], Integer.parseInt(second.split(":")[1])));
			}
		}
		
		return ind;
	}
	
	private List<Determination> extractDeterminations(String detStr) {
		List<Determination> determinations = new LinkedList<Determination>();
		if (!detStr.isEmpty()) {
			String predicate = "";
			int arity = 0;
			String [] dets = detStr.split("\\.");
			for (int i = 0; i < dets.length; i++){
				dets[i] = dets[i].replace("determination(", "").replace(")", "");
				predicate = dets[i].split(",")[1].split("/")[0];
				arity = Integer.parseInt(dets[i].split(",")[1].split("/")[1]);
				determinations.add( new Determination(predicate, arity));
			}
		}
		return determinations;
	}

	private List<Mode> extractModeB(String modeBStr) {
		List<Mode> modes = new LinkedList<Mode>();
		String [] modeBs = modeBStr.split("\\.");
		for (int i = 0; i < modeBs.length; i++){
			modeBs[i] = modeBs[i]/*.replace(".", "")*/.replace(")", "");
			String [] tempArray = modeBs[i].split("\\(");
			String [] args = tempArray[2].split(",");
			List<Argument> arguments = new LinkedList<Argument>();
			for (int j = 0; j < args.length; j++){
				if (args[j].startsWith("+")){
					arguments.add(new Argument(args[j].replace("+", ""), IdentifierType.INPUT));
				} else if (args[j].startsWith("-")){
					arguments.add(new Argument(args[j].replace("-", ""), IdentifierType.OUTPUT));
				} else if (args[j].startsWith("#")){ 
					arguments.add(new Argument(args[j].replace("#", ""), IdentifierType.CONSTANT));
				}
			}
			modes.add(new Mode(tempArray[1].replace("*,", ""), arguments));
		}
		
		return modes;
	}

	private Mode extractModeH(String modeH) {
		List<Argument> arguments = new LinkedList<Argument>();
		String target = "";
		String [] tempArray;
		String [] args;
		modeH = modeH.replace(")", "").replace(".", "");
		tempArray = modeH.split("\\(");
		target = tempArray[1].replace("*,", "");
		args = tempArray[2].split(",");
		for (int i = 0; i < args.length; i++){
			if (args[i].startsWith("+")){
				arguments.add(new Argument(args[i].replace("+", ""), IdentifierType.INPUT));
			} else if (args[i].startsWith("-")){
				arguments.add(new Argument(args[i].replace("-", ""), IdentifierType.OUTPUT));
			} else if (args[i].startsWith("#")){ 
				arguments.add(new Argument(args[i].replace("#", ""), IdentifierType.CONSTANT));
			}
		}
		return new Mode(target, arguments);
	}
	
	@Override
	public Mode getModeH() {
		return modeH;
	}

	@Override
	public List<Mode> getModesB() {
		return modeB;
	}

	@Override
	public List<Determination> getDeterminations() {
		return determinations;
	}

	@Override
	public String getSPNameTemplate() {
		return SPNameTemplate;
	}

	@Override
	public int getIterations() {
		return iteration;
	}
	
	@Override
	public int getRecall() {
		return recall;
	}
	
	@Override
	public int getMaxTerms() {
		return maxterms;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public String getTemporaryTableName() {
		return temparoryTableName;
	}

	@Override
	public Schema getSchema() {
		return schema;
	}

	@Override
	public Map<String, List<InclusionDependency>> getInclusionDep() {
		return schema.getInclusionDependencies();
	}

}
