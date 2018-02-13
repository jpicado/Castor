package castor.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		l.add("brother");
		l.add("brothel");
		l.add("broathe");
		l.add("bro");
//		l.add("vankatesh");
		HSTree hsTree = hs.buildHSTree(l);
//		System.out.println(hsTree.getInvertedIndex().toString());
		System.out.println(hs.hsSearch(hsTree, "brother", 4));
//		System.out.println(hs.generateSubstrings("ssi", hsTree, 11, 2, 1, 0));
//		System.out.println(hs.editDistance("vankatesh", "avataresha"));
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
		
		Map<Integer, Integer> matchSegmentCount = new HashMap<Integer, Integer>();
		int maxLevel = (int)Math.ceil(MyMath.log2(maxDistance+1));
		int minSegments = (int)(Math.pow(2, maxLevel) - maxDistance);
		
		// Use length filter
		for (int l = Math.max(0, query.length()-maxDistance); l <= query.length()+maxDistance; l++) {
			
			if (!hsTree.getInvertedIndex().containsKey(l))
				continue;
			
			if (maxLevel > getMaxLevelOfLengthInHSTree(hsTree, l)) {
				matchingStrings.addAll(getAllStringsOfLengthInHSTree(hsTree, l));
				continue;
			}
			
			if (hsTree.getInvertedIndex().containsKey(l) &&
					hsTree.getInvertedIndex().get(l).containsKey(maxLevel)) {
				
				for (int j=1; j <= Math.pow(2, maxLevel); j++) {
					
					if (hsTree.getInvertedIndex().get(l).get(maxLevel).containsKey(j)) {
					
						List<String> substrings = generateSubstrings(query, hsTree, l, maxLevel, j, maxDistance);
						System.out.println(substrings.toString());
						
						for (String substring : substrings) {
							if (hsTree.getInvertedIndex().get(l).get(maxLevel).get(j).containsKey(substring)) {
								
								for (Integer matchStringIndex : hsTree.getInvertedIndex().get(l).get(maxLevel).get(j).get(substring)) {
									if (matchSegmentCount.containsKey(matchStringIndex)) {
										matchSegmentCount.put(matchStringIndex, matchSegmentCount.get(matchStringIndex)+1);
									} else {
										matchSegmentCount.put(matchStringIndex, 1);
									}
									
									if (matchSegmentCount.get(matchStringIndex) >= minSegments) {
										if (editDistance(hsTree.getStrings().get(matchStringIndex), query) <= maxDistance) {
											matchingStrings.add(hsTree.getStrings().get(matchStringIndex));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return matchingStrings;
	}
	
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
	
	private int getMaxLevelOfLengthInHSTree(HSTree hsTree, int length) {
		int maxLevel = Integer.MIN_VALUE;
		for (Integer i : hsTree.getInvertedIndex().get(length).keySet()) {
			if (i > maxLevel)
				maxLevel = i;
		}
		return maxLevel;
	}
	
	private List<String> generateSubstrings(String query, HSTree hsTree, int l, int i, int j, int maxDistance) {
		List<String> substrings = new ArrayList<String>();
		
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
			substrings.add(query.substring(p, p + segmentLength));
		}
		
		return substrings;
	}
	
	private int editDistance(String string1, String string2) {
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
		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = string1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = string2.charAt(j);
				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
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
		}
		return dp[len1][len2];
	}

	private Pair<String,String> partitionString(String string, int partitionPos) {
		String prefix = string.substring(0, partitionPos);
		String suffix = string.substring(partitionPos, string.length());
		return new Pair<String,String>(prefix, suffix);
	}
}
