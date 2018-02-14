package castor.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import castor.utils.MyMath;
import castor.utils.Pair;

/*
 * Implementation of algorithms in paper "Two Birds With One Stone: An Efficient Hierarchical Framework for Top-k and Threshold-based String Similarity Search".
 */
public class HSAlgorithms {
	
	public static void main(String[] args) {
		HSAlgorithms hs = new HSAlgorithms();
		List<String> l = new ArrayList<String>();
//		l.add("brother");
//		l.add("brothel");
//		l.add("broathe");
//		l.add("bro");
//		l.add("vankatesh");
//		l.add("acompany");
		l.add("are accommodate to");
		HSTree hsTree = hs.buildHSTree(l);
//		System.out.println(hsTree.getInvertedIndex().toString());
		System.out.println(hs.hsSearch(hsTree, "asdw acomofortable", 12));
//		System.out.println(hs.generateSubstrings("ssi", hsTree, 11, 2, 1, 0));
		System.out.println(hs.editDistance("kaushuk chadhui", "caushik chakrabar"));
//		System.out.println(hs.editDistance2("kaushuk chadhui", "caushik chakrabar", 8));
//		System.out.println(hs.isLessThanDistance("kaushuk chadhui", "caushik chakrabar", 3));
	}

	/*
	 * Build an HSTree, containing an inverted index. Entry values of index are index numbers of input strings (instead of storing strings multiple times).
	 */
	public HSTree buildHSTree(List<String> strings) {
		// Map: length -> level -> sibling
		Map<Integer,Map<Integer,Map<Integer,Set<String>>>> stringsByLengthLevelSibling = new HashMap<Integer,Map<Integer,Map<Integer,Set<String>>>>();
		Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex = new HashMap<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>>();
		
		// Group strings by length
		Map<Integer, List<Integer>> stringsByLength = new HashMap<Integer, List<Integer>>();
		int minLength = Integer.MAX_VALUE;
		int maxLength = Integer.MIN_VALUE;
		for (int stringIndex = 0; stringIndex < strings.size(); stringIndex++) {
			String string = strings.get(stringIndex);
			if (!stringsByLength.containsKey(string.length())) {
				stringsByLength.put(string.length(), new ArrayList<Integer>());
			}
			stringsByLength.get(string.length()).add(stringIndex);
			if (string.length() < minLength)
				minLength = string.length();
			if (string.length() > maxLength)
				maxLength = string.length();
		}
		
		for (int l = minLength; l <= maxLength; l++) {
			if (!stringsByLength.containsKey(l))
				continue;
			
			int maxLevel = (int)Math.floor(MyMath.log2(l));
			
			// Crete maps for first level
			stringsByLengthLevelSibling.put(l, new HashMap<Integer,Map<Integer,Set<String>>>());
			stringsByLengthLevelSibling.get(l).put(1, new HashMap<Integer,Set<String>>());
			stringsByLengthLevelSibling.get(l).get(1).put(1, new HashSet<String>());
			stringsByLengthLevelSibling.get(l).get(1).put(2, new HashSet<String>());
			
			invertedIndex.put(l, new HashMap<Integer,Map<Integer,Map<String,List<Integer>>>>());
			invertedIndex.get(l).put(1, new HashMap<Integer,Map<String,List<Integer>>>());
			invertedIndex.get(l).get(1).put(1, new HashMap<String,List<Integer>>());
			invertedIndex.get(l).get(1).put(2, new HashMap<String,List<Integer>>());
			
			// Partition strings in first level
			int prefixLength = (int)Math.floor((double)l/2.0);
			for (Integer stringIndex : stringsByLength.get(l)) {
				String string = strings.get(stringIndex);
				
				Pair<String,String> partitionedString = partitionString(string, prefixLength);
				String firstSegment = partitionedString.getFirst();
				String secondSegment = partitionedString.getSecond();
				
				int firstSiblingIndex = 1;
				int secondSiblingIndex = 2;
				stringsByLengthLevelSibling.get(l).get(1).get(firstSiblingIndex).add(firstSegment);
				stringsByLengthLevelSibling.get(l).get(1).get(secondSiblingIndex).add(secondSegment);
				
				// Update inverted indexes
				if (!invertedIndex.get(l).get(1).get(firstSiblingIndex).containsKey(firstSegment)) 
					invertedIndex.get(l).get(1).get(firstSiblingIndex).put(firstSegment, new ArrayList<Integer>());
				invertedIndex.get(l).get(1).get(firstSiblingIndex).get(firstSegment).add(stringIndex);
				if (!invertedIndex.get(l).get(1).get(secondSiblingIndex).containsKey(secondSegment)) 
					invertedIndex.get(l).get(1).get(secondSiblingIndex).put(secondSegment, new ArrayList<Integer>());
				invertedIndex.get(l).get(1).get(secondSiblingIndex).get(secondSegment).add(stringIndex);
			}
			
			for (int i = 1; i < maxLevel; i++) {
				// Create map for level
				int childrenLevel = i + 1;
				stringsByLengthLevelSibling.get(l).put(childrenLevel, new HashMap<Integer,Set<String>>());
				invertedIndex.get(l).put(childrenLevel, new HashMap<Integer,Map<String,List<Integer>>>());
				
				for (int j = 1; j <= Math.pow(2, i); j++) {
					int firstSiblingIndex = (2*j) - 1;
					int secondSiblingIndex = 2*j;
					
					// Create maps for siblings
					stringsByLengthLevelSibling.get(l).get(childrenLevel).put(firstSiblingIndex, new HashSet<String>());
					stringsByLengthLevelSibling.get(l).get(childrenLevel).put(secondSiblingIndex, new HashSet<String>());
					invertedIndex.get(l).get(childrenLevel).put(firstSiblingIndex, new HashMap<String,List<Integer>>());
					invertedIndex.get(l).get(childrenLevel).put(secondSiblingIndex, new HashMap<String,List<Integer>>());
					
					for (String segment : stringsByLengthLevelSibling.get(l).get(i).get(j)) {
						List<Integer> stringIndexesContainingSegment = invertedIndex.get(l).get(i).get(j).get(segment);
						
						// Partition segment
						int partitionPos = (int)Math.floor((double)segment.length()/2.0);
						Pair<String,String> partitionedString = partitionString(segment, partitionPos);
						String firstSegement = partitionedString.getFirst();
						String secondSegment = partitionedString.getSecond();
						
						stringsByLengthLevelSibling.get(l).get(childrenLevel).get(firstSiblingIndex).add(firstSegement);
						stringsByLengthLevelSibling.get(l).get(childrenLevel).get(secondSiblingIndex).add(secondSegment);
						
						// Update inverted indexes
						if (!invertedIndex.get(l).get(childrenLevel).get(firstSiblingIndex).containsKey(firstSegement)) 
							invertedIndex.get(l).get(childrenLevel).get(firstSiblingIndex).put(firstSegement, new ArrayList<Integer>());
						invertedIndex.get(l).get(childrenLevel).get(firstSiblingIndex).get(firstSegement).addAll(stringIndexesContainingSegment);
						if (!invertedIndex.get(l).get(childrenLevel).get(secondSiblingIndex).containsKey(secondSegment)) 
							invertedIndex.get(l).get(childrenLevel).get(secondSiblingIndex).put(secondSegment, new ArrayList<Integer>());
						invertedIndex.get(l).get(childrenLevel).get(secondSiblingIndex).get(secondSegment).addAll(stringIndexesContainingSegment);
					}
				}
			}
		}
		
		return new HSTree(strings, invertedIndex);
	}
	
	/*
	 * HSSearch algorithm.
	 */
	public Set<String> hsSearch(HSTree hsTree, String query, int maxDistance) {
		Set<String> matchingStrings = new HashSet<String>();
		
		// If maxDistance is 0, get only strings that exactly match with query
		if (maxDistance == 0) {
			if (hsTree.getInvertedIndex().containsKey(query.length())) {
				for (String string : getAllStringsOfLengthInHSTree(hsTree, query.length())) {
					if (query.equals(string))
						matchingStrings.add(string);
				}
			}
		}
		
		Map<Integer, Integer> matchedSegmentsCountForString = new HashMap<Integer, Integer>();
		Map<Integer, List<Pair<String,Integer>>> matchedSegmentsForString = new HashMap<Integer, List<Pair<String,Integer>>>();
		
		int maxLevel = (int)Math.ceil(MyMath.log2(maxDistance+1));
		int minSegments = (int)(Math.pow(2, maxLevel) - maxDistance);
		
		// Use length filter
		for (int l = Math.max(0, query.length()-maxDistance); l <= query.length()+maxDistance; l++) {
			
			if (!hsTree.getInvertedIndex().containsKey(l))
				continue;
			
			// If maxLevel is greater than max level on tree, add all strings, as with the given edit distance, all strings are similar
			if (maxLevel > getMaxLevelOfLengthInHSTree(hsTree, l)) {
				matchingStrings.addAll(getAllStringsOfLengthInHSTree(hsTree, l));
				continue;
			}
			
			if (hsTree.getInvertedIndex().containsKey(l) &&
					hsTree.getInvertedIndex().get(l).containsKey(maxLevel)) {
				
				for (int j=1; j <= Math.pow(2, maxLevel); j++) {
					if (hsTree.getInvertedIndex().get(l).get(maxLevel).containsKey(j)) {
						// Generate substrings of query
						List<Pair<String,Integer>> substrings = generateSubstrings(query, hsTree, l, maxLevel, j, maxDistance);
						
						for (Pair<String,Integer> substringPositionPair : substrings) {
							String substring = substringPositionPair.getFirst();
							
							// Check if substring is a segment in node of tree
							if (hsTree.getInvertedIndex().get(l).get(maxLevel).get(j).containsKey(substring)) {
								// For each string that contains substring, update count of matched segments
								for (Integer matchStringIndex : hsTree.getInvertedIndex().get(l).get(maxLevel).get(j).get(substring)) {
									if (matchedSegmentsCountForString.containsKey(matchStringIndex)) {
										matchedSegmentsCountForString.put(matchStringIndex, matchedSegmentsCountForString.get(matchStringIndex)+1);
										matchedSegmentsForString.get(matchStringIndex).add(substringPositionPair);
									} else {
										matchedSegmentsCountForString.put(matchStringIndex, 1);
										matchedSegmentsForString.put(matchStringIndex, new ArrayList<Pair<String,Integer>>());
										matchedSegmentsForString.get(matchStringIndex).add(substringPositionPair);
									}
								}
							}
						}
					}
				}
				
				// For each string, check if matched segments >= minSegments
				for (Entry<Integer,Integer> entry : matchedSegmentsCountForString.entrySet()) {
					Integer matchStringIndex = entry.getKey();
					Integer count = entry.getValue();
					
					// If count of matched segments >= minSegments, it is a candidate
					if (count >= minSegments) {
						if (hsSearchFilter(hsTree, query, maxDistance, matchStringIndex, matchedSegmentsForString.get(matchStringIndex), minSegments)) {
//						if (editDistance(hsTree.getStrings().get(matchStringIndex), query) <= maxDistance) {
							matchingStrings.add(hsTree.getStrings().get(matchStringIndex));
						}
					}
				}
			}
		}
		
		return matchingStrings;
	}
	
	/*
	 * Remove invalid matchings because of conflicts. 
	 * Returns true if string passes filter and has edit distance <= maxDistance; false otherwise.
	 */
	private boolean hsSearchFilter(HSTree hsTree, String query, int maxDistance, Integer matchStringIndex,  List<Pair<String,Integer>> matchedSegments, int minSegments) {
		int[] matchedSegmentsWithoutConflict = new int[matchedSegments.size()];
		matchedSegmentsWithoutConflict[0] = 1;
		for (int j = 1; j < matchedSegments.size(); j++) {
			int max = Integer.MIN_VALUE;
			for (int t = 0; t <= j - 1; t++) {
				max = Math.max(max, matchedSegmentsWithoutConflict[t] * noConflict(matchedSegments, j, t));
			}
			matchedSegmentsWithoutConflict[j] = max + 1;
		}
		if (matchedSegmentsWithoutConflict[matchedSegments.size() - 1] >= minSegments)  {
			if (editDistance(hsTree.getStrings().get(matchStringIndex), query) <= maxDistance) {
				return true;
			}
		}
		return false;
		
	}
	
	/*
	 * Returns 1 if there are no conflicts between segments j and t in matchedSegments
	 */
	private int noConflict(List<Pair<String, Integer>> matchedSegments, int j, int t) {
		int start1 = matchedSegments.get(j).getSecond();
		int end1 = matchedSegments.get(j).getSecond() + matchedSegments.get(j).getFirst().length()-1;
		int start2 = matchedSegments.get(t).getSecond();
		int end2 = matchedSegments.get(t).getSecond() + matchedSegments.get(t).getFirst().length()-1;
		
		if ((start1 < start2 && end1 >= start2) ||
				(start2 < start1 && end2 >= start1)) {
			return 0;
		}
		return 1;
	}

	/*
	 * Get all strings of given length in tree
	 */
	private List<String> getAllStringsOfLengthInHSTree(HSTree hsTree, int length) {
		Set<Integer> stringIndexes = new HashSet<Integer>();
		for (Integer j : hsTree.getInvertedIndex().get(length).get(1).keySet()) {
			for (String segment : hsTree.getInvertedIndex().get(length).get(1).get(j).keySet()) {
				stringIndexes.addAll(hsTree.getInvertedIndex().get(length).get(1).get(j).get(segment));
			}
		}
		
		List<String> stringsOfLength = new ArrayList<String>();
		for (Integer index : stringIndexes) {
			stringsOfLength.add(hsTree.getStrings().get(index));
		}
		
		return stringsOfLength;
	}
	
	/*
	 * Get max level in tree, for the given length
	 */
	private int getMaxLevelOfLengthInHSTree(HSTree hsTree, int length) {
		int maxLevel = Integer.MIN_VALUE;
		for (Integer i : hsTree.getInvertedIndex().get(length).keySet()) {
			if (i > maxLevel)
				maxLevel = i;
		}
		return maxLevel;
	}
	
	/*
	 * Generate substrings of query based on input and using filters 
	 */
	private List<Pair<String,Integer>> generateSubstrings(String query, HSTree hsTree, int l, int i, int j, int maxDistance) {
		List<Pair<String,Integer>> substrings = new ArrayList<Pair<String,Integer>>();
		
		String someSegment = hsTree.getInvertedIndex().get(l).get(i).get(j).keySet().iterator().next();
		String someString = hsTree.getStrings().get(hsTree.getInvertedIndex().get(l).get(i).get(j).get(someSegment).get(0));
		int segmentLength = someSegment.length();
		int fromIndex = (int)(Math.floor(l / Math.pow(2, i)) * (j-1));
		int segmentStartPosition = someString.indexOf(someSegment, fromIndex);
		int lengthDifference = Math.abs(query.length() - someString.length());
		
		// Shift-based method
//		int lowerBound = Math.max(0, segmentStartPosition - maxDistance);
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + maxDistance);
		
		// Position-aware method
//		int lowerBound = Math.max(0, segmentStartPosition - (int)Math.floor(Math.abs(maxDistance - lengthDifference) / 2));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + (int)Math.floor(Math.abs(maxDistance + lengthDifference) / 2));
		
		// Multi-match-aware left-side perspective
//		int lowerBound = Math.max(0, segmentStartPosition - (j - 1));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + (j - 1));
		
		// Multi-match-aware right-side perspective
//		int lowerBound = Math.max(0, segmentStartPosition + lengthDifference - (maxDistance + 1 - j));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + lengthDifference + (maxDistance + 1 - j));
		
		// Multi-match-aware method
		int lowerBound = Math.max(0, Math.max(segmentStartPosition - (j - 1), segmentStartPosition + lengthDifference - (maxDistance + 1 - j)));
		int upperBound = Math.min(query.length() - segmentLength, Math.min(segmentStartPosition + (j - 1), segmentStartPosition + lengthDifference + (maxDistance + 1 - j)));
		
		for (int p = lowerBound; p <= upperBound && p + segmentLength <= query.length(); p++) {
			substrings.add(new Pair<String,Integer>(query.substring(p, p + segmentLength), p));
		}
		
		return substrings;
	}
	
	/* 
	 * Compute edit distance of two strings using length-aware method.
	 * Returns true if edit distance <= maxDistance.
	 */
	private boolean isLessThanDistance(String string1, String string2, int maxDistance) {
		// Make string2 hold the longer string
		String temp = string2;
		if (string1.length() > string2.length()) {
			string2 = string1;
			string1 = temp;
		}
		
		int len1 = string1.length();
		int len2 = string2.length();
		int lengthDifference = len2 - len1;
		
		System.out.println(string1);
		System.out.println(string2);
		
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
		int[][] expectedEditDistance = new int[len1 + 1][len2 + 1];
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
		
		int lowSubstract = (int)Math.floor(Math.abs(maxDistance - lengthDifference) / 2);
		int highAdd = (int)Math.floor((maxDistance + lengthDifference) / 2);
		
		// Iterate through, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = string1.charAt(i);
			
			int low = Math.max(0, i - lowSubstract);
			int high = Math.min(len2 - 1, i + highAdd);
			
			System.out.println(i+": "+low+","+high);
			
			boolean earlyTermination = true;
			for (int j = low; j <= high; j++) {
//				System.out.println("i:"+i+", j:"+j);
				
				char c2 = string2.charAt(j);
				// If last two chars equal
				if (c1 == c2) {
					// Update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
				
				expectedEditDistance[i + 1][j + 1] = dp[i + 1][j + 1] + Math.abs((len2 - (j+1)) - (len1 - (i+1)));
				
				if (earlyTermination && expectedEditDistance[i + 1][j + 1] <= maxDistance) {
					earlyTermination = false;
				}
			}
			if (earlyTermination) {
				System.out.println("early");
				return false;
			}
			
//			for (int j = 0; j < len2; j++) {
//				System.out.print(j+",");
//			}
//			System.out.println();
			for (int j = 0; j < len2; j++) {
				System.out.print(dp[i][j]+",");
			}
			System.out.println();
			System.out.println("--");
		}
		
		return true;
	}
	public int editDistance2(String string1, String string2, int maxDistance) {
		// Make string2 hold the longer string
		String temp = string2;
		if (string1.length() > string2.length()) {
			string2 = string1;
			string1 = temp;
		}
		int lengthDifference = string2.length() - string1.length();
		
		if (lengthDifference > maxDistance)
			return -1;
		
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];
		int expectedEditDistance[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i < dp[0].length; i++) {
			dp[0][i] = i;
		}

		for (int i = 0; i < dp.length; i++) {
			dp[i][0] = i;
		}
		
		int lowSubstract = (int)(Math.abs(maxDistance - lengthDifference) / 2);
		int highAdd = (int)((maxDistance + lengthDifference) / 2);
		System.out.println(lowSubstract);
		System.out.println(highAdd);
		
		for (int j = 0; j < string2.length(); j++) {
			System.out.print(dp[0][j]+",");
		}
		System.out.println();
		System.out.println("--");

		for (int i = 1; i <= string1.length(); i++) {
			char c1 = string1.charAt(i - 1);
			
			int low = Math.max(1, i - lowSubstract);
			int high = Math.min(string2.length(), i + highAdd);
			
			System.out.println(i+":"+low+","+high);
			
			boolean earlyTermination = true;
//			for (int j = 1; j <= string2.length(); j++) {
			for (int j = low; j <= high; j++) {
				
				char c2 = string2.charAt(j - 1);
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
//					dp[i][j] = 1 + dp[i - 1][j - 1];
				}
				
				expectedEditDistance[i][j] = dp[i][j] + Math.abs((string2.length() - j) - (string1.length() - i));
				if (earlyTermination && expectedEditDistance[i][j] <= maxDistance) {
					earlyTermination = false;
				}
			}
			
			if (earlyTermination) {
				return -1;
			}
			
			for (int j = 0; j < string2.length(); j++) {
				System.out.print(dp[i][j]+",");
			}
			System.out.println();
			System.out.println("--");
		}
		return dp[string1.length()][string2.length()];
	}
	
	
	
	/* 
	 * Compute edit distance of two strings.
	 * Length-aware method.
	 */
	public int editDistance(String string1, String string2) {
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i < dp[0].length; i++) {
			dp[0][i] = i;
		}

		for (int i = 0; i < dp.length; i++) {
			dp[i][0] = i;
		}

		for (int i = 1; i <= string1.length(); i++) {
			char c1 = string1.charAt(i - 1);
			for (int j = 1; j <= string2.length(); j++) {
				char c2 = string2.charAt(j - 1);
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
				}
			}
		}
		return dp[string1.length()][string2.length()];
	}

	/* 
	 * Compute edit distance of two strings.
	 * Length-aware method.
	 */
	/*private int editDistance(String string1, String string2) {
		int len1 = string1.length();
		int len2 = string2.length();
		
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
		
		// Iterate through, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = string1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = string2.charAt(j);
				// If last two chars equal
				if (c1 == c2) {
					// Update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
			
			for (int j = 0; j < len2; j++) {
				System.out.print(j+",");
			}
			System.out.println();
			for (int j = 0; j < len2; j++) {
				System.out.print(dp[i][j]+",");
			}
			System.out.println();
			System.out.println("--");
		}
		
		return dp[len1][len2];
	}*/
	
	/*
	 * Partition string at position and return parts
	 */
	private Pair<String,String> partitionString(String string, int partitionPos) {
		String prefix = string.substring(0, partitionPos);
		String suffix = string.substring(partitionPos, string.length());
		return new Pair<String,String>(prefix, suffix);
	}
}
