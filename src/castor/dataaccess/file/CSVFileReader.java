package castor.dataaccess.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import castor.language.Tuple;

public class CSVFileReader {
	
	/*
	 * Read a CSV file and return its contents
	 * Assume that file contains header, so skip first line
	 */
	public static List<Tuple> readCSV(String csvFile) {
		List<Tuple> tuples = new LinkedList<Tuple>();
		CSVReader reader = null;
        try {
            reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(csvFile)))
            		.withSkipLines(1)
            		.build();

            String[] line;
            while ((line = reader.readNext()) != null) {
                List<Object> values = Arrays.asList((Object[]) line);
                tuples.add(new Tuple(values));
            }
        } catch (IOException e) {
        		throw new RuntimeException(e.getMessage());
        } finally {
        		try {
        			if (reader != null)
        				reader.close();
        		} catch (Exception e) {}
        }
        
        return tuples;
	}
	
	/*
	 * Read the header of a CSV file (assumed to be the first line)
	 */
	public static List<String> readCSVHeader(String csvFile) {
		List<String> header = null;
		CSVReader reader = null;
        try {
        		reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(csvFile)))
            		.build();

            String[] line;
            if ((line = reader.readNext()) != null) {
            		header = Arrays.asList(line);
            }
        } catch (IOException e) {
        		throw new RuntimeException(e.getMessage());
        } finally {
        		try {
        			if (reader != null)
        				reader.close();
        		} catch (Exception e) {}
        }
        
        return header;
	}
}
