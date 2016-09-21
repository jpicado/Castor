package castor.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FileUtils {

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
}
