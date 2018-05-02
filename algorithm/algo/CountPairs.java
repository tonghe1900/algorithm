package algo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CountPairs {

	
	
	static int countPairs(int a[], int x) {
		Map<Integer, Integer> map = createMap(a);
		
		int count = 0;
		Set<Integer> set = new HashSet<>();
		for(Integer key: map.keySet()){
			int val = x - key;
			if(!set.contains(key)){
				if(map.containsKey(val)){
					if(val != key){
						count += map.get(val)* map.get(key);
					}else{
						count += chooseTwo(map.get(val));
					}
					set.add(val);
				}
				
			}
			
			
		}
		

		return count; 
		  }


	private static Map<Integer, Integer> createMap(int[] a) {
		Map<Integer, Integer> map = new HashMap<>();
		for(int element :a ){
			if(!map.containsKey(element)){
				map.put(element, 1);
			}else{
				map.put(element, map.get(element) +1);
			}
		}
		return map;
	}
	
	
	private static int chooseTwo(int total) {
		if(total == 1) return 0;
		
		return total *  (total - 1) /2;
	}


	public static void main(String[]args){
		
		String s = "http://localhost:8080/admin/kettle/";
		String []  firstSplits = s.split("//");
		String [] secondSplits = firstSplits[1].split("/");
		System.out.println(Arrays.asList(s.split("//")));
		System.out.println(Arrays.asList(secondSplits));
		System.out.println(firstSplits[0]+"//"+secondSplits[0]);
		junit.framework.Assert.assertEquals(9, countPairs(new int[]{1,2,3,4,2,2,2,1,1}, 4));
		junit.framework.Assert.assertEquals(15, countPairs(new int[]{1,3,3,2,3,4,2,2,2,1,1}, 4));
		junit.framework.Assert.assertEquals(21, countPairs(new int[]{2,2,2,2,2,2,2}, 4));
		junit.framework.Assert.assertEquals(15, countPairs(new int[]{1,3,1,1,1,1,3,3}, 4));
		junit.framework.Assert.assertEquals(8, countPairs(new int[]{-1,5,-1,5,-2,6,-2,6,4}, 4));
	}
}
