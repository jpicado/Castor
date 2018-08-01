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
public class HSTreeCreator {
	
	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
//		l.add("brother");
//		l.add("brothel");
//		l.add("broathe");
//		l.add("bro");
//		l.add("vankatesh");
//		l.add("acompany");
//		l.add("are accommodate to");
//		l.add("ovner loevi");
//		l.add("swingable");
		l.add("Avengers: Age of Ultron (2015)");
		HSTree hsTree = HSTreeCreator.buildHSTree(l);
//		System.out.println(hsTree.getInvertedIndex().toString());
		
		System.out.println(hsTree.getInvertedIndex().toString());
		
		System.out.println(hsTree.hsSearch("Avengers: Age of Ultron", 8).toString());
		System.out.println(SimilarityUtils.editDistance("Avengers: Age of Ultron", "Avengers: Age of Ultron (2015)"));
		
//		System.out.println(hs.generateSubstrings("abna levina", hsTree, 7, 2, 1, 9));
//		System.out.println(hs.editDistance("ovner loevi", "abna levina"));
//		System.out.println(hs.editDistance("abna levina", "vankatesh"));
//		System.out.println(hs.isLessThanDistance("ovner loevi", "abna levina", 7));
//		System.out.println(hs.isLessThanDistance("bro", "brother", 3));
//		System.out.println(hs.isLessThanDistance2("bro", "brother", 3));
		
		
//		RandomSet<Pair<String,Integer>> matchedSegments = new RandomSet<Pair<String,Integer>>();
//		matchedSegments.add(new Pair<String,Integer>("a",0));
//		matchedSegments.add(new Pair<String,Integer>("a",3));
//		matchedSegments.add(new Pair<String,Integer>("a",10));
//		matchedSegments.add(new Pair<String,Integer>("n",9));
//		System.out.println(hs.hsSearchVerificationMultiExtension("ovner loevi", "abna levina", 8, 3, matchedSegments));
//		System.out.println(hs.getUnmatchedSegments("acompany", matchedSegments));
////		int[] orders = {3,5,6};
////		System.out.println(hs.getUnmatchedSegments("abna levina", orders, matchedSegments));
	}

	/*
	 * Build an HSTree, containing an inverted index. Entry values of index are index numbers of input strings (instead of storing strings multiple times).
	 */
	public static HSTree buildHSTree(List<String> strings) {
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
			
			// Create maps for first level
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
	 * Partition string at position and return parts
	 */
	private static Pair<String,String> partitionString(String string, int partitionPos) {
		String prefix = string.substring(0, partitionPos);
		String suffix = string.substring(partitionPos, string.length());
		return new Pair<String,String>(prefix, suffix);
	}
}
