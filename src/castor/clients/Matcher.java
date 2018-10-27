package castor.clients;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import castor.similarity.HSTree;
import castor.similarity.HSTreeCreator;
import castor.similarity.SimilarValue;

public class Matcher {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	public static void main(String[] args) throws Exception {

		String csvFile1 = args[0];
		String csvFile2 = args[1];
		String output = args[2];

		List<String> bom_movies = new LinkedList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(csvFile1)));
			String line;
			while ((line = br.readLine()) != null) {
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

		System.out.println("BOM movies:" + bom_movies.size());
		HSTree tree = HSTreeCreator.buildHSTree(bom_movies);

		br = null;
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(new File(csvFile2)));
			String line;
			while ((line = br.readLine()) != null) {
				List<String> parsed = parseLine(line);
				String id = parsed.get(0);
				String title = parsed.get(1).substring(2, parsed.get(1).length());
				String year = parsed.get(2).substring(2, parsed.get(2).length());

				Set<SimilarValue> similarValues = tree.hsSearch(title, 10);

				String newTitle = title;
				if (!similarValues.isEmpty()) {
					PriorityQueue<SimilarValue> heap = new PriorityQueue<SimilarValue>(similarValues.size(),
							Comparator.comparing(SimilarValue::getDistance).thenComparing(SimilarValue::getValue));
					// PriorityQueue<SimilarValue> heap = new
					// PriorityQueue<SimilarValue>(similarValues.size(), new MyComparator());
					heap.addAll(similarValues);

					int minDistance = heap.peek().getDistance();

					List<String> topSimilarTitles = new ArrayList<String>();
					while (!heap.isEmpty() && heap.peek().getDistance() == minDistance) {
						topSimilarTitles.add(heap.poll().getValue());
					}

					int randomIndex = random.nextInt(topSimilarTitles.size());
					newTitle = topSimilarTitles.get(randomIndex);

					System.out.println(title + "---->" + newTitle);
				}
				String tuple = id + ", \"" + newTitle + "\", \"" + year + "\"";
				sb.append(tuple + "\n");
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

		FileWriter fileWriter = new FileWriter(output);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(sb.toString());
		printWriter.close();
	}

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
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

					// Fixed : allow "" in custom quote enclosed
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

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
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
