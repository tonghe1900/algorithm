import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;




public class Test {
  public static void main(String [] args){
	  //System.out.println(new Test().isPossible(new int[]{ 1,2,3,3,4,4,5,5}));
//	  double test = 18.24e-8;
//	  // test = 1;
//	  String str =  new Double(test).toString();
//	  
//	  System.out.println(Double.parseDouble(str));
//	 
//	  if(str.contains("E")){
//		 int numberOfZero = 0;
//		 String []  splits =  str.split("E");
//		 if(splits[0].contains(".")){
//			 System.out.println(Arrays.asList(splits[0].split("\\.")));
//			 numberOfZero += splits[0].split("\\.")[1].length();
//		 }
//		 if(splits[1].startsWith("-")){
//			 numberOfZero += Integer.parseInt(splits[1].split("-")[1]);
//		 }
//		 System.out.println(numberOfZero);
//		 StringBuilder formatter = new StringBuilder("#0.");
//		 for(int i=0 ; i<numberOfZero ;i+=1){
//			 formatter.append("0");
//		  }
//		  NumberFormat format = new DecimalFormat(formatter.toString());     
//		  System.out.println(format.format(test));
//		  System.out.println(test);
//	  }else{
//		  System.out.println(str);
//	  }
//	  System.out.println(new BigDecimal(new Double(test).toString()));
//	  StringBuilder sb = new StringBuilder();
//	  String s = "\\";
//	  		//"//"\\\\\"t\\\\\"";
//	  for(int i=0;i<s.length();i+=1){
//		  char ch = s.charAt(i);
//		  System.out.println((int)ch);
//		  
//			  sb.append(ch);
//		 
//		  
//	  }
//	  System.out.println(sb.toString());
//	  Double issueCost = 1.0;
//	  DecimalFormat df = new DecimalFormat("0");
//		df.setMaximumFractionDigits(340);
//		System.out.println(df.format(issueCost));
	  String test="IT I&O Manager";
	  System.out.println(test.toLowerCase());
		// 0.0000000124   0.0000000124     1.24   1
//		String output = DoubleFormatter.INSTANCE.format(1.24e-8);
//    	assertEquals("0.0000000124", output);
//    }
//    
//    
//    @Test
//    public void testDoubleFormat2(){
//    	String output = DoubleFormatter.INSTANCE.format(1.24E-8);
//    	assertEquals("0.0000000124", output);
//    }
//    
//    
//    @Test
//    public void testDoubleFormat3(){
//    	String output = DoubleFormatter.INSTANCE.format(1.24);
//    	assertEquals("1.24", output);
//    }
//    
//    
//    @Test
//    public void testDoubleFormat4(){
//    	String output = DoubleFormatter.INSTANCE.format(1.0);
//    	assertEquals("1.0", output);
	
  }
	 

  
  
  private HashMap<Integer, PriorityQueue<Integer>> dmap;
  public boolean isPossible(int[] nums) {
      dmap = new HashMap<>();
      for (int num : nums) {
          PriorityQueue<Integer> pq0 = getOrPut(num - 1);
          int len = pq0.isEmpty() ? 0 : pq0.poll();
          PriorityQueue<Integer> pq1 = getOrPut(num);
          pq1.offer(len + 1);
      }
      for (int key : dmap.keySet()) {
          for (int len : dmap.get(key)) {
              if (len < 3) return false;
          }
      }
      return true;
  }
  public PriorityQueue<Integer> getOrPut(int num) {
      PriorityQueue<Integer> pq = dmap.get(num);
      if (pq == null) {
          pq = new PriorityQueue<Integer>();
          dmap.put(num, pq);
      }
      return pq;
  }
  
  
  
////  1. We iterate through the array once to get the frequency of all the elements in the array
////  2. We iterate through the array once more and for each element we either see if it can be appended to a previously constructed consecutive sequence or if it can be the start of a new consecutive sequence. If neither are true, then we return false.
//  public boolean isPossible1(int[] nums) {
//      Map<Integer, Integer> freq = new HashMap<>(), appendfreq = new HashMap<>();
//      for (int i : nums) freq.put(i, freq.getOrDefault(i,0) + 1);
//      for (int i : nums) {
//          if (freq.get(i) == 0) continue;
//          else if (appendfreq.getOrDefault(i,0) > 0) {
//              appendfreq.put(i, appendfreq.get(i) - 1);
//              appendfreq.put(i+1, appendfreq.getOrDefault(i+1,0) + 1);
//          }   
//          else if (freq.getOrDefault(i+1,0) > 0 && freq.getOrDefault(i+2,0) > 0) {
//              freq.put(i+1, freq.get(i+1) - 1);
//              freq.put(i+2, freq.get(i+2) - 1);
//              appendfreq.put(i+3, appendfreq.getOrDefault(i+3,0) + 1);
//          }
//          else return false;
//          freq.put(i, freq.get(i) - 1);
//      }
//      return true;
//  }
  
  
  
}
