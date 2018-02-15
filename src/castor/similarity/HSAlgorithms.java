package castor.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.util.CombinatoricsUtils;

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
		l.add("vankatesh");
		l.add("acompany");
		l.add("are accommodate to");
		HSTree hsTree = hs.buildHSTree(l);
//		System.out.println(hsTree.getInvertedIndex().toString());
		
//		System.out.println(hs.hsSearch(hsTree, "bro", 1));
		
//		System.out.println(hs.generateSubstrings("ssi", hsTree, 11, 2, 1, 0));
		System.out.println(hs.editDistance("ovner loevi", "abna levina"));
//		System.out.println(hs.isLessThanDistance("kaushuk chadhui", "caushik chakrabar", 10));
		
		
		List<Pair<String,Integer>> matchedSegments = new ArrayList<Pair<String,Integer>>();
		matchedSegments.add(new Pair<String,Integer>("n",0));
		matchedSegments.add(new Pair<String,Integer>("l",0));
		matchedSegments.add(new Pair<String,Integer>("ev",0));
		System.out.println(hs.hsSearchVerificationSingleExtension("ovner loevi", "abna levina", 8, 3, matchedSegments));
//		System.out.println(hs.getUnmatchedSegments("ovner loevi", matchedSegments));
////		int[] orders = {3,5,6};
////		System.out.println(hs.getUnmatchedSegments("abna levina", orders, matchedSegments));
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
						if (hsSearchFilter(hsTree, query, maxDistance, maxLevel, matchStringIndex, matchedSegmentsForString.get(matchStringIndex), minSegments)) {
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
	private boolean hsSearchFilter(HSTree hsTree, String query, int maxDistance, int level, Integer matchStringIndex,  List<Pair<String,Integer>> matchedSegments, int minSegments) {
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
//			if (editDistance(hsTree.getStrings().get(matchStringIndex), query) <= maxDistance) {
//			if (isLessThanDistance(hsTree.getStrings().get(matchStringIndex), query, maxDistance)) {
			if (hsSearchVerificationSingleExtension(query, hsTree.getStrings().get(matchStringIndex), maxDistance, level, matchedSegments)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hsSearchVerificationSingleExtension(String query, String candidate, int maxDistance, int level, List<Pair<String,Integer>> matchedSegments) {
		int count = matchedSegments.size();
		int minSegmentsMinusDistance = (int)(Math.pow(2, level) - maxDistance);
		if (count < minSegmentsMinusDistance)
			return false;
		else if (CombinatoricsUtils.binomialCoefficient(count, minSegmentsMinusDistance) >= candidate.length()) {
			if (isLessThanDistance(candidate, query, maxDistance))
				return true;
			else
				return false;
		} else {
			// Get unmatched segments
			List<String> candidateUnmatchedSegments = getUnmatchedSegments(candidate, matchedSegments);
			List<String> queryUnmatchedSegments = getUnmatchedSegments(query, matchedSegments);
			
			int totalDistance = 0;
			for (int i=0; i<candidateUnmatchedSegments.size(); i++) {
				totalDistance += editDistance(candidateUnmatchedSegments.get(i), queryUnmatchedSegments.get(i));
			}
			
			if (totalDistance <= maxDistance)
				return true;
			return false;
		}
	}
	
	//TODO there's some bug, returning incorrect answer for hsSearchVerificationSingleExtension("ovner loevi", "abna levina", 6, 3, matchedSegments)
	private boolean hsSearchVerificationMultiExtension(String query, String candidate, int maxDistance, int level, List<Pair<String,Integer>> matchedSegments) {
		int count = matchedSegments.size();
		
		int minSegmentsMinusDistance = (int)(Math.pow(2, level) - maxDistance);
		if (count < minSegmentsMinusDistance) {
			return false;
		} else if (CombinatoricsUtils.binomialCoefficient(count, minSegmentsMinusDistance) >= candidate.length()) {
			if (isLessThanDistance(candidate, query, maxDistance))
				return true;
			else
				return false;
		} else {
			// Get segments of candidate at current level
			List<String> candidateSegments = new ArrayList<String>();
			candidateSegments.add(candidate);
			for (int i=1; i<=level; i++) {
				List<String> newCandidateSegments = new ArrayList<String>();
				for (int j=0; j<candidateSegments.size(); j++) {
					Pair<String,String> partitioned = partitionString(candidateSegments.get(j), (int)(candidateSegments.get(j).length()/2));
					newCandidateSegments.add(partitioned.getFirst());
					newCandidateSegments.add(partitioned.getSecond());
				}
				candidateSegments = newCandidateSegments;
			}
			
			// Get unmatched segments
			List<String> candidateUnmatchedSegments = getUnmatchedSegments(candidate, matchedSegments);
			List<String> queryUnmatchedSegments = getUnmatchedSegments(query, matchedSegments);
			
			System.out.println(candidateUnmatchedSegments.toString());
			System.out.println(queryUnmatchedSegments.toString());
			
			// Calculate orders (orders[i] = the order of matchedSegments[i] among candidateSegments)
			int[] orders = new int[count];
			int candidateSegmentIndex = 0;
			for (int i=0; i < orders.length; i++) {
				while (candidateSegmentIndex < candidateSegments.size()) {
					if (matchedSegments.get(i).getFirst().equals(candidateSegments.get(candidateSegmentIndex))) {
						orders[i] = candidateSegmentIndex+1;// adding 1 to be the same as in paper
						candidateSegmentIndex++;
						break;
					}
					candidateSegmentIndex++;
				}
			}
			
			// Calculate thresholds
			int[] thresholds = new int[count+1];
			thresholds[0] = orders[0] - 1;
			thresholds[count] = (int)Math.pow(2, level) - orders[count-1];
			for (int i=1; i<count; i++) {
				thresholds[i] = orders[i] - orders[i-1] - 1;
			}
			
			for (int i = 0; i < thresholds.length; i++) {
				System.out.print(thresholds[i]+ " ");
			}
			System.out.println();
			
			for (int i=0; i <= count; i++) {
				if (!isLessThanDistance(candidateUnmatchedSegments.get(i), queryUnmatchedSegments.get(i), thresholds[i]))
					return false;
			}
			
			return true;
		}
	}
	
	private List<String> getUnmatchedSegments(String string, List<Pair<String,Integer>> matchedSegments) {
		List<String> unmatchedSegments = new ArrayList<String>();
		
		int stringIndex = 0;
		int currentMatchedSegmentIndex = 0;
		while (stringIndex < string.length() && currentMatchedSegmentIndex < matchedSegments.size()) {
			int matchedSegmentIndex = string.indexOf(matchedSegments.get(currentMatchedSegmentIndex).getFirst(), stringIndex);
			unmatchedSegments.add(string.substring(stringIndex, matchedSegmentIndex));
			
			stringIndex = matchedSegmentIndex + matchedSegments.get(currentMatchedSegmentIndex).getFirst().length();
			currentMatchedSegmentIndex++;
		}
		unmatchedSegments.add(string.substring(stringIndex, string.length()));
		
		return unmatchedSegments;
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
	 * Compute edit distance of two strings. 
	 * Improvement 2: length-aware verification pruning and early termination using expected edit distance.
	 * Returns true if edit distance <= maxDistance.
	 */
	private boolean isLessThanDistance(String string1, String string2, int maxDistance) {
		// Make string2 hold the longer string
		String temp = string2;
		if (string1.length() > string2.length()) {
			string2 = string1;
			string1 = temp;
		}
		int lengthDifference = string2.length() - string1.length();
		
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];
		int expectedEditDistance[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i <= Math.min(string1.length()-1, maxDistance); i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= Math.min(string2.length()-1, maxDistance); j++) {
			dp[0][j] = j;
		}
		
		int lowSubstract = (int)Math.floor(Math.abs(maxDistance - lengthDifference) / 2) + 1;
		int highAdd = (int)Math.floor((maxDistance + lengthDifference) / 2);

		for (int i = 1; i <= string1.length(); i++) {
			char c1 = string1.charAt(i - 1);
			boolean earlyTermination = true;
			
			int low = Math.max(1, i - lowSubstract);
			int high = Math.min(string2.length(), i + highAdd);
			
			for (int j = low; j <= high; j++) {
				char c2 = string2.charAt(j - 1);
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
//					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
					int insertDeleteOp = 0;
					if (i == j)
						insertDeleteOp = Math.min(dp[i - 1][j], dp[i][j - 1]);
					else if (i < j)
						insertDeleteOp = dp[i][j-1];
					else
						insertDeleteOp = dp[i-1][j];
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], insertDeleteOp);
				}
				
				expectedEditDistance[i][j] = dp[i][j] + Math.abs((string2.length() - j) - (string1.length() - i));
				if (earlyTermination && expectedEditDistance[i][j] <= maxDistance) {
					earlyTermination = false;
				}
			}
			
			if (earlyTermination) {
				return false;
			}
		}
		return true;
	}
	
	/* 
	 * Compute edit distance of two strings. 
	 * Improvement 1: length pruning and early termination using with prefix pruning.
	 * Returns incorrect result.
	 */
	/*private boolean isLessThanDistance2(String string1, String string2, int maxDistance) {
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i <= maxDistance; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= maxDistance; j++) {
			dp[0][j] = j;
		}

		for (int i = 1; i <= string1.length(); i++) {
			char c1 = string1.charAt(i - 1);
			boolean earlyTermination = true;
			int low = Math.max(1, i-maxDistance);
			int high = Math.min(string2.length(), Math.abs(i+maxDistance));
			for (int j = low; j <= high; j++) {
				char c2 = string2.charAt(j - 1);
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
//					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
					int insertDeleteOp = 0;
					if (i == j)
						insertDeleteOp = Math.min(dp[i - 1][j], dp[i][j - 1]);
					else if (i < j)
						insertDeleteOp = dp[i][j-1];
					else
						insertDeleteOp = dp[i-1][j];
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], insertDeleteOp);
				}
				
				if (earlyTermination && dp[i][j] <= maxDistance) {
					earlyTermination = false;
				}
			}
			
			if (earlyTermination) {
				return false;
			}
		}
		return true;
	}*/
	
	/* 
	 * Compute edit distance of two strings. 
	 * Original dynamic programming algorithm: no improvements and no early termination.
	 */
	private int editDistance(String string1, String string2) {
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i < dp.length; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j < dp[0].length; j++) {
			dp[0][j] = j;
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
	 * Partition string at position and return parts
	 */
	private Pair<String,String> partitionString(String string, int partitionPos) {
		String prefix = string.substring(0, partitionPos);
		String suffix = string.substring(partitionPos, string.length());
		return new Pair<String,String>(prefix, suffix);
	}
}
