package castor.similarity;

public class SimilarityUtils {

	/* 
	 * Compute edit distance of two strings. 
	 * Original dynamic programming algorithm: no improvements and no early termination.
	 */
	public static int editDistance(String string1, String string2) {
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
	 * Compute edit distance of two strings.
	 * Improvement 1: only compute values M[i,j] for 2*maxDistance+1 values in a row. If all of them are > maxDistance, terminate.
	 * Returns true if edit distance <= maxDistance.
	 */
	public static boolean isLessThanDistanceImprovement1(String string1, String string2, int maxDistance) {
		// If length difference is > maxDistance, return false. Otherwise, compute values
		if (Math.abs(string1.length() - string2.length()) > maxDistance) {
			return false;
		}
		
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];
		for (int i = 0; i <= Math.min(dp.length-1, maxDistance); i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= Math.min(dp[0].length-1, maxDistance); j++) {
			dp[0][j] = j;
		}
		
		int highestJ = 0;
		for (int i = 1; i <= string1.length(); i++) {
			char c1 = string1.charAt(i - 1);
			
			boolean allValuesLargerThanDistance = true;
			
			int low = Math.max(1, i - maxDistance);
			int high = Math.min(string2.length(), i + maxDistance);
			highestJ = Math.max(highestJ, high);
			for (int j = low; j <= high; j++) {
				char c2 = string2.charAt(j - 1);
				if (c1 == c2) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
//					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
					if (j == low) {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], dp[i - 1][j]);
					} else if (j == high) {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], dp[i][j - 1]);
					} else {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
					}
				}
				if (dp[i][j] <= maxDistance) {
					allValuesLargerThanDistance = false;
				}
			}
			
			if (allValuesLargerThanDistance) {
				return false;
			}
		}
		
		// Check final alignment
		if (dp[string1.length()][highestJ] > maxDistance) {
			return false;
		}
		
		return true;
	}
	
	/* 
	 * Compute edit distance of two strings. 
	 * Improvement 2: length-aware verification pruning and early termination using expected edit distance.
	 * Returns true if edit distance <= maxDistance.
	 */
	public static boolean isLessThanDistance(String string1, String string2, int maxDistance) {
		// Make string2 hold the longer string
		String temp = string2;
		if (string1.length() > string2.length()) {
			string2 = string1;
			string1 = temp;
		}
		int lengthDifference = string2.length() - string1.length();
		if (lengthDifference > maxDistance) {
			return false;
		}
		
		int dp[][] = new int[string1.length() + 1][string2.length() + 1];
		int expectedEditDistance[][] = new int[string1.length() + 1][string2.length() + 1];

		for (int i = 0; i <= Math.min(string1.length()-1, maxDistance); i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= Math.min(string2.length()-1, maxDistance); j++) {
			dp[0][j] = j;
		}
		
		int lowSubstract = (int)Math.floor(Math.abs(maxDistance - lengthDifference) / 2);// + 1;
		int highAdd = (int)Math.floor((maxDistance + lengthDifference) / 2);
		int highestJ = 0;
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
					if (j == low) {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], dp[i - 1][j]);
					} else if (j == high) {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], dp[i][j - 1]);
					} else {
						dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
					}
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
		
		// Check final alignment
		if (dp[string1.length()][highestJ] > maxDistance) {
			return false;
		}
		
		return true;
	}
}
