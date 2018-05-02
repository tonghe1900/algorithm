
/**
 * 某寺庙里7个和尚每天轮流挑水，每个人可以挑水的日期是固定的
例如：
和尚1： 星期二，四；
和尚2： 星期一，六；
和尚3： 星期三，日；
和尚4： 星期五
和尚5： 星期一，四，六；
和尚6： 星期二，五；
和尚7， 星期三，六，日。
需要给出所有的挑水方案。 
 
维护人: 
g00121837
 
输入：
 
7个和尚的可以挑水的日期。一个7*7二维数组数组，数组值为1表示和尚当天可以挑水，值为0表示当天不能挑水。 
 
输出：
 
挑水方案总数
生成的挑水方案
如输入：{ {0, 1, 0, 1, 0, 0, 0}, {1, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 1}, 
        {0, 0, 0, 0, 1, 0, 0}, {1, 0, 0, 1, 0, 1, 0}, {0, 1, 0, 0, 1, 0, 0},
        {0, 0, 1, 0, 0, 1, 1} };
应可生成4种方案。
方按到星期1，星期2....星期7的和尚序号（和尚序号从1开始）升序排列。 
方案1: 星期1和尚2挑水，星期2和尚6挑水，星期3和尚3挑水，星期4和尚1挑水，星期5和尚4挑水，星期6和尚5挑水，星期天和尚7挑水。 
方案2: 星期1和尚2挑水，星期2和尚6挑水，星期3和尚7挑水，星期4和尚1挑水，星期5和尚4挑水，星期6和尚5挑水，星期天和尚3挑水。 
方案3: 星期1和尚5挑水，星期2和尚6挑水，星期3和尚3挑水，星期4和尚1挑水，星期5和尚4挑水，星期6和尚2挑水，星期天和尚7挑水。 
方案4: 星期1和尚5挑水，星期2和尚6挑水，星期3和尚7挑水，星期4和尚1挑水，星期5和尚4挑水，星期6和尚2挑水，星期天和尚3挑水。
 
样例输入：
 
0 1 0 1 0 0 0
1 0 0 0 0 1 0
0 0 1 0 0 0 1
0 0 0 0 1 0 0
1 0 0 1 0 1 0
0 1 0 0 1 0 0
0 0 1 0 0 1 1

 
样例输出：
 
4
2 6 3 1 4 5 7
2 6 7 1 4 5 3
5 6 3 1 4 2 7
5 6 7 1 4 2 3
 
 
发件人: he tong [mailto:hetong1900@gmail.com] 
 */




import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//worst time complexity is O(n!), n factorial,   n is the number of persons 
//space complexity is O(7*n)  =O(n)  n is the number of persons
public class Main {
   public static void getStrategies(int [][] input){
	   checkParameter(input);
	   List<Integer[]> result =generateStrategies(input);
	   
	   System.out.println(result.size());
	   for(Integer[] subResult: result){
		   for(Integer i: subResult){
			   System.out.print(i +" ");
		   }
		   System.out.println();
	   }
	}
   
   
   private static void checkParameter(int[][] input) {
	  for(int [] arr: input){
		  if(arr.length != 7){
			  throw new IllegalArgumentException();
		  }
		  for(int i=0;i<arr.length;i+=1){
			  if(arr[i] != 1 && arr[i] != 0){
				  throw new IllegalArgumentException();
			  }
		  }
	  }
	
}


public static List<Integer[]> generateStrategies(int [][] input){
	   Map<Integer, Set<Integer>> map = getPersonsForCertainDayMapping(input);
	   List<Integer[]> result = new ArrayList<>();
	   if(input.length <7) return result;
	   Set<Integer> alreadySelectedPersons = new HashSet<>();
	   Integer[] currentResult = new Integer[7];
	   generateStrategies(1, map, result,alreadySelectedPersons,currentResult);
	   return result;
   }
   
   private static void generateStrategies(int day, Map<Integer, Set<Integer>> map,
		List<Integer[]> result, Set<Integer> alreadySelectedPersons, Integer[] currentResult) {
	  if(day == 8) {
		  result.add(Arrays.copyOfRange(currentResult, 0, currentResult.length));
		  return;
	  }
	  Set<Integer> persons =  map.get(day);
	  Set<Integer> availablePersons = getAvailablePersons(persons, alreadySelectedPersons);
	  if(availablePersons.isEmpty()) return;
	  for(Integer avaliablePerson: availablePersons){
		  currentResult[day-1] = avaliablePerson;
		  alreadySelectedPersons.add(avaliablePerson);
		  generateStrategies(day+1, map, result, alreadySelectedPersons, currentResult);
		  alreadySelectedPersons.remove(avaliablePerson);
	  }
	
}

private static Set<Integer> getAvailablePersons(Set<Integer> persons,
		Set<Integer> alreadySelectedPersons) {
	Set<Integer> result  = new HashSet<>();
	if(persons == null || persons.isEmpty()) return result;
	for(Integer person: persons){
		if(!alreadySelectedPersons.contains(person)){
			result.add(person);
		}
	}
	return result;
}

private static  Map<Integer, Set<Integer>> getPersonsForCertainDayMapping(int [][] input){
	   Map<Integer, Set<Integer>> result = new HashMap<>();
	   for(int i=0; i< input.length; i+=1){
		   for(int j=0 ;j<input[i].length ; j+=1){
			   if(input[i][j] ==1){
				   Set<Integer> set;
				   if(result.containsKey(j+1)){
					   set = result.get(j+1);
				   }else{
					   set = new HashSet<>();
				   }
				   set.add(i+1);
				   result.put(j+1, set);
			   }
		   }
	   }
	   return result;
	   
   }
	public static void main(String[] args) {
		int [][] input = new int[][]{ {0, 1, 0, 1, 0, 0, 0}, {1, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 1}, 
		        {0, 0, 0, 0, 1, 0, 0}, {1, 0, 0, 1, 0, 1, 0}, {0, 1, 0, 0, 1, 0, 0},
		        {0, 0, 1, 0, 0, 1, 1} };
		getStrategies(input);
//		System.out.println();
//		System.out.println("The following is testing the other input.");
//		input = new int[][]{ {0, 0, 1, 0, 0, 0, 1} };
//		getStrategies(input);
//		System.out.println();
//		input = new int[][]{ {0, 1, 0, 1, 0, 0, 0}, {0, 1, 0, 1, 0, 0, 0}, {0, 1, 0, 1, 0, 0, 0}, 
//				{0, 1, 0, 1, 0, 0, 0}, {0, 1, 0, 1, 0, 0, 0}, {0, 1, 0, 1, 0, 0, 0},
//		        {0, 0, 1, 0, 0, 1, 1} };
//		getStrategies(input);
//		System.out.println();
//		input = new int[][]{ {1, 1, 1, 1, 1, 1, 1},  {1, 1, 1, 1, 1, 1, 1},  {1, 1, 1, 1, 1, 1, 1}, 
//				 {1, 1, 1, 1, 1, 1, 1},{1, 1, 1, 1, 1, 1, 1},{1, 1, 1, 1, 1, 1, 1} };
//		getStrategies(input);
//		System.out.println();
//		
//		
//		input = new int[][]{ {0, 1, 0, 1, 0, 0, 0}, {1, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 1}, 
//		        {0, 0, 0, 0, 1, 0, 0}, {1, 0, 0, 1, 0, 1, 0}, {0, 1, 0, 0, 1, 0, 0},
//		        {0, 0, 1, 0, 0, 1, 1} , {0, 0, 1, 0, 0, 1, 1} , {0, 0, 1, 0, 0, 1, 1} };
//		getStrategies(input);
//		
//		input = new int[][]{ {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, 
//				{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1} , {1, 1, 1, 1, 1, 1, 1} , {1, 1, 1, 1, 1, 1, 1} };
//		getStrategies(input);
		

	}

}
