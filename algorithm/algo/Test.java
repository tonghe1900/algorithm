package algo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum Test {
   FRIST("he"), SECOND("tong");
   private String symbol;

  Test(String symbol) {
	this.symbol =  symbol;
}
@Override
public String toString() {
	return this.symbol;
}
private class MinRate{
	private int val;
	private int left;
	private int right;
	public MinRate(int val, int left, int right) {
		super();
		this.val = val;
		this.left = left;
		this.right = right;
	}
	public MinRate() {
		super();
	}
	
}
//   1 2 3 4 5       1 2 3 2 1    minCandy(i, j)  minCandy(i-1, j)  = minCandy(i, j)+1  if rates[i-1]>rates[i]
private class Segment{//  if rates[i-1]<rates[i]  // if 
      private MinRate minRate;
      private int leftBoundary;
      private int rightBoundary;
	public Segment(MinRate minRate, int leftBoundary, int rightBoundary) {
		super();
		this.minRate = minRate;
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}
	public Segment() {
		super();
	}
      
      
}


public int minCandy(int [] rates){
	//  
	if(rates == null || rates.length <1) return 0;
	if(rates.length == 1) return 1;
	if(rates.length ==2 ) return rates[0]==rates[1]? 2 : 3;
	Map<Integer, List<Segment>> segments = generateSegments(rates);
	
	
}

private Map<Integer, List<Segment>> generateSegments(int[] rates) {
	// TODO Auto-generated method stub
	return null;
}
private int[][] getMin(int[] rates){
	int length = rates.length;
	
	int [][] map = new int[length][length];
	for(int index=0;index<length;index++){
		map[index][index] = rates[index];
	}
	for(int len=2; len<=length;len++){
		for(int begin = 0; begin < length -len+1;begin++){
			int end = begin +len -1;
			map[begin][end] = Math.min(map[begin][end-1], rates[end]);
		}
	}
	return map;
	
}

private int minCandy(int [] rates, int i, int j){
	
}


public static void main(String [] args){
	//https://gravitant.atlassian.net/browse/CM-16474
	
	String str ="CM-tongk (ggggererwqewegttttttttttttttrrrrrrrrrrrrrrrrreeeeeeeeeeeeeeeeeeeeee)";
	System.out.println(Arrays.asList("fffff".split(" ")));
	System.out.println(transform(str));
}
private static String transform(String str) {
	String [] splits = str.split(" ");
	if(splits.length ==1){
		return str;
	}
	String firstPart = splits[0];
	String secondPart = splits[1];
	if(secondPart.startsWith("(") && secondPart.endsWith(")")){
		secondPart = secondPart.substring(1, secondPart.length()-1);
		if(secondPart.length() > 30){
			secondPart = "("+secondPart.substring(0, 30)+"...)";
			System.out.println(secondPart.length());
		}
	}
	return firstPart + " "+secondPart;
}


   
}
