//package test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

// 303        //12345678

/**
 *     	Given a string of digits, find the minimum number of additions required for the string to equal some target number. Each addition is the equivalent of inserting a plus sign somewhere into the string of digits. After all plus signs are inserted, evaluate the sum as usual. For example, consider the string "12" (quotes for clarity). With zero additions, we can achieve the number 12. If we insert one plus sign into the string, we get "1+2", which evaluates to 3. So, in that case, given "12", a minimum of 1 addition is required to get the number 3. As another example, consider "303" and a target sum of 6. The best strategy is not "3+0+3", but "3+03". You can do this because leading zeros do not change the result.
Write a class QuickSums that contains the method minSums, which takes a String numbers and an int sum. The method should calculate and return the minimum number of additions required to create an expression from numbers that evaluates to sum. If this is impossible, return -1.
 
Definition
    	
Class:	QuickSums
Method:	minSums
Parameters:	String, int
Returns:	int
Method signature:	int minSums(String numbers, int sum)
(be sure your method is public)
    
 
Constraints
-	numbers will contain between 1 and 10 characters, inclusive.
-	Each character in numbers will be a digit.
-	sum will be between 0 and 100, inclusive.
 
Examples
0)	


	System.out.println( new CopyOfCopyOfQuickSums().minSums("11", 2));   // 1
	System.out.println( new CopyOfCopyOfQuickSums().minSums("0123456789", 45));  //8
	System.out.println( new CopyOfCopyOfQuickSums().minSums("99999", 45));   //4
	System.out.println( new CopyOfCopyOfQuickSums().minSums("1110", 3));   //3
	System.out.println( new CopyOfCopyOfQuickSums().minSums("99999", 100));  //-1
	System.out.println( new CopyOfCopyOfQuickSums().minSums("382834", 100));  //2
	System.out.println( new CopyOfCopyOfQuickSums().minSums("9230560001", 71)); //4
    	
"99999"
45
Returns: 4
In this case, the only way to achieve 45 is to add 9+9+9+9+9. This requires 4 additions.
1)	
    	
"1110"
3
Returns: 3
Be careful with zeros. 1+1+1+0=3 and requires 3 additions.
2)	
    	
"0123456789"
45
Returns: 8
3)	
    	
"99999"
100
Returns: -1
4)	
    	
"382834"
100
Returns: 2
There are 3 ways to get 100. They are 38+28+34, 3+8+2+83+4 and 3+82+8+3+4. The minimum required is 2.
5)	
    	
"9230560001"
71
Returns: 4
 * @author het
 *
 */
 
	
	
	

public class CopyOfCopyOfQuickSums {
  public  int minSums(String numbers, int sum){
	  Map<Integer, Set<Integer>> map = possibleSums(numbers, 0, numbers.length()-1, new HashMap<String, Map<Integer, Set<Integer>>>());
	  Set<Integer> set = map.get(sum);
	  if(set == null) return -1;
	  return  map.get(sum).toArray(new Integer[]{})[0];

  }
  private String createKey(int begin, int end){
	  return begin + ","+ end;
  }
  
  private Map<Integer, Set<Integer>>  possibleSums(String numbers, int begin, int end, Map<String, Map<Integer, Set<Integer>>> map){
	  String key = createKey(begin, end);
	if(map.containsKey(key)) {
		  return map.get(key);
	  }
	  if(begin == end) {
		  Map<Integer, Set<Integer>> result = new HashMap<>();
		  Set<Integer> set = new TreeSet<>();
		  set.add(0);
		  result.put(Integer.parseInt(numbers.substring(begin, begin+1)), set);
		  
		  map.put(key, result);
		  return result;
	  }
	  Map<Integer, Set<Integer>> result = new HashMap<>();
	  for(int i= begin+1; i<= end; i+=1){
		  Map<Integer, Set<Integer>>  firstResult = possibleSums(numbers, begin, i-1, map);
		  Map<Integer, Set<Integer>>  secondResult = possibleSums(numbers, i, end, map);
		  for(Integer firstKey: firstResult.keySet()){
			  for(Integer secondKey: secondResult.keySet()){
				  
				 
				  
				int sumVal = firstKey + secondKey;
				if(sumVal <=100 && sumVal >=0){
					process(result, firstResult, secondResult, firstKey, secondKey,
							sumVal, true);
				}
				
				String combined = firstKey +""+secondKey;
				if(combined.length() <=3){
					process(result, firstResult, secondResult, firstKey, secondKey,
							Integer.parseInt(combined), false);
				}
			  }
		  }
		  
		  
		  
		  
	  }
	  map.put(key, result);
	  
	 
	  return result;
  }
private void process(Map<Integer, Set<Integer>> result,
		Map<Integer, Set<Integer>> firstResult,
		Map<Integer, Set<Integer>> secondResult, Integer firstKey,
		Integer secondKey, int sumVal, boolean addPlus) {
	if(!result.containsKey(sumVal)){
		  result.put(sumVal, new TreeSet<Integer>());
	  }
	  Set<Integer>  set = result.get(sumVal);
	  set.addAll(createCombinedSet(firstResult.get(firstKey) , secondResult.get(secondKey), addPlus));
}



private Set<Integer> createCombinedSet(Set<Integer> first, Set<Integer> second ,boolean addPlus) {
	Set<Integer> result = new TreeSet<>();
	for(Integer firstElement:first){
		for(Integer secondElement : second){
			result.add(firstElement+secondElement+(addPlus? 1: 0));
		}
	}
	return result;
}


public static void main(String [] args){
	System.out.println( new CopyOfCopyOfQuickSums().minSums("11", 2));   // 1
	System.out.println( new CopyOfCopyOfQuickSums().minSums("0123456789", 45));  //8
	System.out.println( new CopyOfCopyOfQuickSums().minSums("99999", 45));   //4
	System.out.println( new CopyOfCopyOfQuickSums().minSums("1110", 3));   //3
	System.out.println( new CopyOfCopyOfQuickSums().minSums("99999", 100));  //-1
	System.out.println( new CopyOfCopyOfQuickSums().minSums("382834", 100));  //2
	System.out.println( new CopyOfCopyOfQuickSums().minSums("9230560001", 71)); //4
	System.out.println( new CopyOfCopyOfQuickSums().minSums("0000", 71));
	System.out.println( new CopyOfCopyOfQuickSums().minSums("0000", 0));
	System.out.println( new CopyOfCopyOfQuickSums().minSums("0100", 1));
}


}


