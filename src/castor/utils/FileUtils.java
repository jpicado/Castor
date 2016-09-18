package castor.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileUtils {

	public static JsonObject convertFileToJSON(String fileName){
        // Read from File to String
        JsonObject jsonObject = new JsonObject();
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(fileName));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (Exception e) {
        	e.printStackTrace();
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
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return jsonObject;
	}
}
