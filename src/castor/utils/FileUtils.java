package castor.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FileUtils {

	public static void writeToFile(String filename, List<String> lines) {
		try {
			Path file = Paths.get(filename);
			Files.write(file, lines, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static JsonObject convertFileToJSON(String fileName){
        // Read from File to String
        JsonObject jsonObject = new JsonObject();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement;
		try {
			jsonElement = parser.parse(new FileReader(fileName));
			jsonObject = jsonElement.getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
        return jsonObject;
    }
	
	public static JsonObject convertStreamToJSON(InputStream stream) {
		JsonObject jsonObject = null;
        try {
            JsonParser parser = new JsonParser();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8")); 
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null) {
            	strBuilder.append(line);
            }
            
            JsonElement jsonElement = parser.parse(strBuilder.toString());
            jsonObject = jsonElement.getAsJsonObject();
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
        return jsonObject;
	}

    public static boolean writeModeToJsonFormat(String target, String headMode, List<String> bodyMode, String dbName, String filePath) {
        JsonObject job = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je0 = jp.parse(gson.toJson(target));
        JsonElement je1 = jp.parse(gson.toJson(headMode));
        JsonElement je2 = jp.parse(gson.toJson(bodyMode));
        JsonElement je3 = jp.parse(gson.toJson(dbName));
        job.add("target", je0);
        job.add("headMode", je1);
        job.add("bodyModes", je2);
        job.add("spName", je3);
        return writeGsonToFile(filePath,gson.toJson(job));
    }

    public static boolean writeIndsToJsonFormat(List<String> ind, Map<String, Integer> ordPos, String filePath) {
        JsonObject jobParent = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (String insString : ind) {
            String[] line = insString.split(Constants.Regex.SUBSET.getValue());
            String uNode = line[0].trim();
            uNode = uNode.substring(1, uNode.length() - 1);
            String vNode = line[1].trim();
            vNode = vNode.substring(1, vNode.length() - 1);

            JsonObject jobChild = new JsonObject();
            String[] un = uNode.split(Pattern.quote(Constants.Regex.PERIOD.getValue()));
            String[] vn = vNode.split(Pattern.quote(Constants.Regex.PERIOD.getValue()));
            jobChild.addProperty("leftRelation", un[0]);
            jobChild.addProperty("leftAttributeNumber", ordPos.get(uNode));
            jobChild.addProperty("rightRelation", vn[0]);
            jobChild.addProperty("rightAttributeNumber", ordPos.get(vNode));
            JsonElement element = gson.fromJson(jobChild.toString(), JsonElement.class);
            jsonArray.add(element);
        }
        jobParent.add("inds", jsonArray);
        return writeGsonToFile(filePath,gson.toJson(jobParent));
    }

    public static boolean writeGsonToFile(String filePath, String fileContent){
        try {
            FileWriter file = new FileWriter(filePath);
            file.write(fileContent);
            file.flush();
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
