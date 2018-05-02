import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Problem Statement for ShortPalindromes


Problem Statement
    	
A palindrome is a String that is spelled the same forward and backwards. Given a String base that may or may not be a palindrome, we can always force base to be a palindrome by adding letters to it. For example, given the word "RACE", we could add the letters "CAR" to its back to get "RACECAR" (quotes for clarity only). However, we are not restricted to adding letters at the back. For example, we could also add the letters "ECA" to the front to get "ECARACE". In fact, we can add letters anywhere in the word, so we could also get "ERCACRE" by adding an 'E' at the beginning, a 'C' after the 'R', and another 'R' before the final 'E'. Your task is to make base into a palindrome by adding as few letters as possible and return the resulting String. When there is more than one palindrome of minimal length that can be made, return the lexicographically earliest (that is, the one that occurs first in alphabetical order).

 
Definition
    	
Class:	ShortPalindromes
Method:	shortest
Parameters:	String
Returns:	String
Method signature:	String shortest(String base)
(be sure your method is public)
    
 
Constraints
-	base contains between 1 and 25 characters, inclusive.
-	Every character in base is an uppercase letter ('A'-'Z').
 
Examples
0)	
    	
"RACE"
Returns: "ECARACE"
To make "RACE" into a palindrome, we must add at least three letters. However, there are eight ways to do this by adding exactly three letters:
    "ECARACE"  "ECRARCE"  "ERACARE"  "ERCACRE"
    "RACECAR"  "RAECEAR"  "REACAER"  "RECACER"
Of these alternatives, "ECARACE" is the lexicographically earliest.
1)	
    	
"TOPCODER"

TOPCODER

REDTOPCODER

REDOTOPCODER
REDOCPOTOPCODER

REDTOCPCOTDER
Returns: "REDTOCPCOTDER"
2)	
    	
"Q"
Returns: "Q"
3)	
    	
"MADAMIMADAM"
Returns: "MADAMIMADAM"
4)	
    	
"ALRCAGOEUAOEURGCOEUOOIGFA"
Returns: "AFLRCAGIOEOUAEOCEGRURGECOEAUOEOIGACRLFA"
This problem statement is the exclusive and proprietary property of TopCoder, Inc. Any unauthorized use or reproduction of this information without the prior written consent of TopCoder, Inc. is strictly prohibited. (c)2010, TopCoder, Inc. All rights reserved.




This problem was used for: 
       Single Round Match 165 Round 1 - Division II, Level Three
 * @author het
 *
 */

//TOPCODER
// abcdefg
//  gfedcba bcdefg
//  abcdefg fedcba
//    acbca 
//      cabac
public class ShortPalindromes {
  public String shortest(String word){
	  Set<String> set = palindromes(word, 0, word.length() -1, new HashMap<String, Set<String>>());
	  //System.out.println(set);
	  //Integer.MIN_VALUE
	  return set.toArray(new String[]{})[0];
  }
  
  private String createKey(int begin, int end){
	  return begin + ","+ end;
  }
  
  private Set<String> createTreeSet(){
	  Comparator<String> comparator = new Comparator<String>(){

		@Override
		public int compare(String str1, String str2) {
			int str1Length  =str1.length();
			int str2Length  =str2.length();
			if(str1Length == str2Length) return str1.compareTo(str2);
			else  return new Integer(str1.length()).compareTo(new Integer(str2.length()));
			
		}};
	return new TreeSet<>(comparator);
	  
  }
  
  
  private Set<String> palindromes(String word,int begin, int end, Map<String, Set<String>> cache){
	  
	    String key = createKey(begin, end);
	    if(cache.containsKey(key)) return cache.get(key);
	    if(begin  > end){
	    	Set<String> set = createTreeSet();
	    	set.add("");
	    	cache.put(key, set);
	    	return set;
	    }
	    
	    if(begin == end) {
	    	Set<String> set = createTreeSet();
	    	set.add(word.substring(begin, begin+1));
	    	cache.put(key, set);
	    	return set;
	    }
	    char beginChar = word.charAt(begin);
	    char endChar = word.charAt(end);
	    Set<String> set = createTreeSet();
	    if(beginChar == endChar) {
	    	Set<String> subResult = palindromes(word, begin+1, end-1, cache);
	    	
	    	for(String element: subResult){
	    		set.add(beginChar + element +  beginChar);
	    	}
	    }else{
	    	
	    	Set<String> subResultForFrontAdd = palindromes(word, begin, end-1, cache);
	    	
	    	
	    	Set<String> subResultForEndAdd = palindromes(word, begin+1, end, cache);
	    	int firstlengthFront = getFirst(subResultForFrontAdd).length();
			int firstlengthEnd = getFirst(subResultForEndAdd).length();
			if(firstlengthFront < firstlengthEnd){
	    		for(String element: subResultForFrontAdd){
		    		set.add(endChar + element + endChar);
		    	}
	    	}else if(firstlengthFront > firstlengthEnd){
	    		for(String element: subResultForEndAdd){
		    		set.add(beginChar + element+beginChar);
		    	}
	    	}else{
	    		for(String element: subResultForFrontAdd){
		    		set.add(endChar + element + endChar);
		    	}
	    		
	    		for(String element: subResultForEndAdd){
		    		set.add(beginChar + element+beginChar);
		    	}
	    	}
	    	
	    	
	    }
	    cache.put(key, set);
	    return set;
  }
  
  private String getFirst(Set<String> set){
	  return set.toArray(new String[]{})[0];
  }
  
  
  // @include
  public static int maxSubarraySumInCircular(List<Integer> A) {
    return Math.max(findMaxSubarray(A), findCircularMaxSubarray(A));
  }

  // Calculates the non-circular solution.
  private static int findMaxSubarray(List<Integer> A) {
    int maximumTill = 0, maximum = 0;
    for (Integer a : A) {
      maximumTill = Math.max(a, a + maximumTill);
      maximum = Math.max(maximum, maximumTill);
    }
    return maximum;
  }

  // Calculates the solution which is circular.
  private static int findCircularMaxSubarray(List<Integer> A) {
    // Maximum subarray sum starts at index 0 and ends at or before index i.
    ArrayList<Integer> maximumBegin = new ArrayList<>();
    int sum = A.get(0);
    maximumBegin.add(sum);
    for (int i = 1; i < A.size(); ++i) {
      sum += A.get(i);
      maximumBegin.add(
          Math.max(maximumBegin.get(maximumBegin.size() - 1), sum));
    }

    // Maximum subarray sum starts at index i + 1 and ends at the last element.
    List<Integer> maximumEnd
        = new ArrayList<>(Collections.nCopies(A.size(), 0));
    sum = 0;
    for (int i = A.size() - 2; i >= 0; --i) {
      sum += A.get(i + 1);
      maximumEnd.set(i, Math.max(maximumEnd.get(i + 1), sum));
    }

    // Calculates the maximum subarray which is circular.
    int circularMax = 0;
    for (int i = 0; i < A.size(); ++i) {
      circularMax
          = Math.max(circularMax, maximumBegin.get(i) + maximumEnd.get(i));
    }
    return circularMax;
  }
  // @exclude

  // O(n^2) solution
  private static int checkAns(List<Integer> A) {
    int ans = 0;
    for (int i = 0; i < A.size(); ++i) {
      int sum = 0;
      for (int j = 0; j < A.size(); ++j) {
        sum += A.get((i + j) % A.size());
        ans = Math.max(ans, sum);
      }
    }
    System.out.println("correct answer = " + ans);
    return ans;
  }

//  public static void main(String[] args) {
//    Random r = new Random();
//    for (int times = 0; times < 1000; ++times) {
//      int n;
//      ArrayList<Integer> A = new ArrayList<>();
//      if (args.length > 1) {
//        for (int i = 1; i < args.length; ++i) {
//          A.add(Integer.parseInt(args[i]));
//        }
//      } else {
//        if (args.length == 1) {
//          n = Integer.parseInt(args[0]);
//        } else {
//          n = r.nextInt(10000) + 1;
//        }
//        while (n-- != 0) {
//          A.add(r.nextInt(20001) - 10000);
//        }
//      }
//      int ans = maxSubarraySumInCircular(A);
//      // System.out.println(A);
//      System.out.println("maximum sum = " + ans);
//      assert(ans == checkAns(A));
//    }
//  }
  
  public static void main(String [] args){
	  System.out.println(new ShortPalindromes().shortest("ADVB"));
	  System.out.println(new ShortPalindromes().shortest("TOPCODER"));
	  System.out.println(new ShortPalindromes().shortest("RACE"));
	  System.out.println(new ShortPalindromes().shortest("Q"));
	  System.out.println(new ShortPalindromes().shortest("MADAMIMADAM"));
	  System.out.println(new ShortPalindromes().shortest("ALRCAGOEUAOEURGCOEUOOIGFA"));
	  System.out.println("AFLRCAGIOEOUAEOCEGRURGECOEAUOEOIGACRLFA".equals("AFLRCAGIOEOUAEOCEGRURGECOEAUOEOIGACRLFA"));
	  
  }
 }
