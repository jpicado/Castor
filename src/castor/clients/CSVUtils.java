package castor.clients;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void main(String[] args) throws Exception {

        //String csvFile = "/Users/picadolj/Desktop/test.csv";
    	String csvFile1 = "/Users/picadolj/Desktop/csvs/movies2totalgross_bom2.csv";
    	String csvFile2 = "/Users/picadolj/Desktop/csvs/movies2.csv";
    	
    	int count = 0;

//        while (scanner.hasNext()) {
//        	System.out.println(scanner.nextLine());
////            List<String> line = parseLine(scanner.nextLine());
////            System.out.println(line.size() +":"+line.toString());
////            if (line.size() != 3)
////            	System.out.println(line.toString());
////            System.out.println("Country [id= " + line.get(0) + ", code= " + line.get(1) + " , name=" + line.get(2) + "]");
//            count++;
//        }
    	
    	List<String> bom_movies = new LinkedList<String>();
    	BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(csvFile1)));
            String line;
            while((line = br.readLine()) != null) {
            	List<String> parsed = parseLine(line);
            	bom_movies.add(parsed.get(0));
            }
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            if (br != null) {
               try {
                  br.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
        
        System.out.println("BOM movies:"+bom_movies.size());
        
        br = null;
        try {
            br = new BufferedReader(new FileReader(new File(csvFile2)));
            String line;
            while((line = br.readLine()) != null) {
            	List<String> parsed = parseLine(line);
            	System.out.println(parsed.get(0) +","+ parsed.get(1)+","+parsed.get(2));
            	count++;
            }
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            if (br != null) {
               try {
                  br.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
        
        System.out.println(count);
    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}
