package castor.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.math3.util.CombinatoricsUtils;

import castor.utils.MyMath;
import castor.utils.Pair;
import castor.utils.RandomSet;

public class HSTree {

	private List<String> strings;
	private Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex;

	public HSTree(List<String> strings, Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex) {
		super();
		this.strings = strings;
		this.invertedIndex = invertedIndex;
	}
	
	public List<String> getStrings() {
		return strings;
	}

	public Map<Integer, Map<Integer, Map<Integer, Map<String, List<Integer>>>>> getInvertedIndex() {
		return invertedIndex;
	}
	
	/*
	 * HSSearch algorithm.
	 */
	public Set<String> hsSearch(String query, int maxDistance) {
		Set<String> matchingStrings = new HashSet<String>();
		
		// If maxDistance is 0, get only strings that exactly match with query
		if (maxDistance == 0) {
			if (invertedIndex.containsKey(query.length())) {
				for (String string : getAllStringsOfLengthInHSTree(query.length())) {
					if (query.equals(string))
						matchingStrings.add(string);
				}
			}
		}
		
		Map<Integer, Integer> matchedSegmentsCountForString = new HashMap<Integer, Integer>();
		Map<Integer, RandomSet<Pair<String,Integer>>> matchedSegmentsForString = new HashMap<Integer, RandomSet<Pair<String,Integer>>>();
		
		// Use length filter
		for (int l = Math.max(0, query.length()-maxDistance); l <= query.length()+maxDistance; l++) {
			
			if (!invertedIndex.containsKey(l))
				continue;
			
			int maxLevelInTree = Integer.MIN_VALUE;
			for (Integer level : invertedIndex.get(l).keySet()) {
				maxLevelInTree = Math.max(maxLevelInTree, level);
			}
			
			int maxLevel = Math.min(maxLevelInTree, (int)Math.ceil(MyMath.log2(maxDistance+1)));
			int minSegments = (int)(Math.pow(2, maxLevel) - maxDistance);
			
			if (invertedIndex.containsKey(l) &&
					invertedIndex.get(l).containsKey(maxLevel)) {
				 
				for (int j=1; j <= Math.pow(2, maxLevel); j++) {
					if (invertedIndex.get(l).get(maxLevel).containsKey(j)) {
						// Generate substrings of query
						List<Pair<String,Integer>> substrings = generateSubstrings(query, l, maxLevel, j, maxDistance);
						
						for (Pair<String,Integer> substringPositionPair : substrings) {
							String substring = substringPositionPair.getFirst();
							
							// Check if substring is a segment in node of tree
							if (invertedIndex.get(l).get(maxLevel).get(j).containsKey(substring)) {
								// For each string that contains substring, update count of matched segments
								for (Integer matchStringIndex : invertedIndex.get(l).get(maxLevel).get(j).get(substring)) {
									if (matchedSegmentsCountForString.containsKey(matchStringIndex)) {
										matchedSegmentsCountForString.put(matchStringIndex, matchedSegmentsCountForString.get(matchStringIndex)+1);
										matchedSegmentsForString.get(matchStringIndex).add(substringPositionPair);
									} else {
										matchedSegmentsCountForString.put(matchStringIndex, 1);
										matchedSegmentsForString.put(matchStringIndex, new RandomSet<Pair<String,Integer>>());
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
						if (SimilarityUtils.editDistance(strings.get(matchStringIndex), query) <= maxDistance) {
//						if (SimilarityUtils.isLessThanDistance(strings.get(matchStringIndex), query, maxDistance)) {
//						if (hsSearchFilter(query, maxDistance, maxLevel, matchStringIndex, matchedSegmentsForString.get(matchStringIndex), minSegments)) {
							matchingStrings.add(strings.get(matchStringIndex));
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
	private boolean hsSearchFilter(String query, int maxDistance, int level, Integer matchStringIndex,  RandomSet<Pair<String,Integer>> matchedSegments, int minSegments) {
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
			//TODO Currently using length-aware verification because other methods have bugs 
//			if (editDistance(strings.get(matchStringIndex), query) <= maxDistance) {
			if (SimilarityUtils.isLessThanDistance(strings.get(matchStringIndex), query, maxDistance)) {
//			if (hsSearchVerificationSingleExtension(query, strings.get(matchStringIndex), maxDistance, level, matchedSegments)) {
//			if (hsSearchVerificationMultiExtension(query, strings.get(matchStringIndex), maxDistance, level, matchedSegments)) {
				return true;
			}
		}
		return false;
	}
	
	//TODO Must try different alignments.
	private boolean hsSearchVerificationSingleExtension(String query, String candidate, int maxDistance, int level, RandomSet<Pair<String,Integer>> matchedSegments) {
		int count = matchedSegments.size();
		int minSegmentsMinusDistance = (int)(Math.pow(2, level) - maxDistance);
		if (count < minSegmentsMinusDistance) {
			return false;
		} else if (CombinatoricsUtils.binomialCoefficient(count, minSegmentsMinusDistance) >= candidate.length()) {
			if (SimilarityUtils.isLessThanDistance(candidate, query, maxDistance))
				return true;
			else
				return false;
		} else {
			// Get unmatched segments
			List<String> candidateUnmatchedSegments = getUnmatchedSegments(candidate, matchedSegments);
			List<String> queryUnmatchedSegments = getUnmatchedSegments(query, matchedSegments);
			
			int totalDistance = 0;
			for (int i=0; i<candidateUnmatchedSegments.size(); i++) {
				totalDistance += SimilarityUtils.editDistance(candidateUnmatchedSegments.get(i), queryUnmatchedSegments.get(i));
			}
			
			if (totalDistance <= maxDistance)
				return true;
			return false;
		}
	}
	
	//TODO Must try different alignments.
	//TODO there's some bug, returning incorrect answer for hsSearchVerificationSingleExtension("ovner loevi", "abna levina", 7, 3, matchedSegments)
	private boolean hsSearchVerificationMultiExtension(String query, String candidate, int maxDistance, int level, RandomSet<Pair<String,Integer>> matchedSegments) {
		int count = matchedSegments.size();
		
		int minSegmentsMinusDistance = (int)(Math.pow(2, level) - maxDistance);
		if (count < minSegmentsMinusDistance) {
			return false;
		} else if (CombinatoricsUtils.binomialCoefficient(count, minSegmentsMinusDistance) >= candidate.length() ||
				count > minSegmentsMinusDistance) {
			if (SimilarityUtils.isLessThanDistance(candidate, query, maxDistance))
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
			
//			for (int i = 0; i < thresholds.length; i++) {
//				System.out.print(thresholds[i]);
//			}
//			System.out.println();
			
			for (int i=0; i <= count; i++) {
				if (!SimilarityUtils.isLessThanDistance(candidateUnmatchedSegments.get(i), queryUnmatchedSegments.get(i), thresholds[i]))
					return false;
			}
			
			return true;
		}
	}
	
	//TODO Must try different alignments. Getting error for input: acompany, [< a , 0 > , < a , 3 > , < a , 10 > , < n , 9 > ]
	private List<String> getUnmatchedSegments(String string, RandomSet<Pair<String,Integer>> matchedSegments) {
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
	private int noConflict(RandomSet<Pair<String, Integer>> matchedSegments, int j, int t) {
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
	private List<String> getAllStringsOfLengthInHSTree(int length) {
		Set<Integer> stringIndexes = new HashSet<Integer>();
		for (Integer j : invertedIndex.get(length).get(1).keySet()) {
			for (String segment : invertedIndex.get(length).get(1).get(j).keySet()) {
				stringIndexes.addAll(invertedIndex.get(length).get(1).get(j).get(segment));
			}
		}
		
		List<String> stringsOfLength = new ArrayList<String>();
		for (Integer index : stringIndexes) {
			stringsOfLength.add(strings.get(index));
		}
		
		return stringsOfLength;
	}
	
	/*
	 * Generate substrings of query based on input and using filters 
	 */
	private List<Pair<String,Integer>> generateSubstrings(String query, int l, int i, int j, int maxDistance) {
		List<Pair<String,Integer>> substrings = new ArrayList<Pair<String,Integer>>();
		
		String someSegment = invertedIndex.get(l).get(i).get(j).keySet().iterator().next();
		String someString = strings.get(invertedIndex.get(l).get(i).get(j).get(someSegment).get(0));
		int segmentLength = someSegment.length();
		int fromIndex = (int)(Math.floor(l / Math.pow(2, i)) * (j-1));
		int segmentStartPosition = someString.indexOf(someSegment, fromIndex);
		int lengthDifference = Math.abs(query.length() - someString.length());
		
		//TODO currently using position-aware method because other methods have bugs
		
		// Shift-based method
//		int lowerBound = Math.max(0, segmentStartPosition - maxDistance);
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + maxDistance);
		
		// Position-aware method
		int lowerBound = Math.max(0, segmentStartPosition - (int)Math.floor(Math.abs(maxDistance - lengthDifference) / 2));
		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + (int)Math.floor(Math.abs(maxDistance + lengthDifference) / 2));
		
		// Multi-match-aware left-side perspective
//		int lowerBound = Math.max(0, segmentStartPosition - (j - 1));
//		int lowerBound = Math.max(0, segmentStartPosition - Math.abs(maxDistance - lengthDifference));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + (j - 1));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + maxDistance - lengthDifference);
		
		// Multi-match-aware right-side perspective
//		int lowerBound = Math.max(0, segmentStartPosition + lengthDifference - (maxDistance + 1 - j));
//		int upperBound = Math.min(query.length() - segmentLength, segmentStartPosition + lengthDifference + (maxDistance + 1 - j));
		
		// Multi-match-aware method
//		int lowerBound = Math.max(0, Math.max(segmentStartPosition - (j - 1), segmentStartPosition + lengthDifference - (maxDistance + 1 - j)));
//		int upperBound = Math.min(query.length() - segmentLength, Math.min(segmentStartPosition + (j - 1), segmentStartPosition + lengthDifference + (maxDistance + 1 - j)));
		
		for (int p = lowerBound; p <= upperBound && p + segmentLength <= query.length(); p++) {
			substrings.add(new Pair<String,Integer>(query.substring(p, p + segmentLength), p));
		}
		
		return substrings;
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
