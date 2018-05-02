import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
public class Solution {
	
	

	public static void main(String args[] )  {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */

		
		BufferedReader  br = null;
		PrintWriter pw  = null;
		try{
			
		
		 br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
		String oneline = br.readLine();
		int lineNum = 1;
		Integer totalLineNum = null;
		Set<Integer> set = new HashSet<>();
		while(oneline!= null ){
			if(lineNum == 1){
				try {
					totalLineNum = Integer.parseInt(oneline);
				} catch (NumberFormatException e) {
					outMsgForWrongInput(pw, lineNum);
				}
			}else if(oneline.trim().length() == 0){
				outMsgForWrongInput(pw, lineNum);
			}else if(totalLineNum == null || lineNum > totalLineNum+1){
				outMsgForWrongInput(pw, lineNum);
			}else{
				String [] splits = oneline.split(" ");
				boolean allValidDigits = checkAllValidDigits(splits);
				if(!allValidDigits){
					outMsgForWrongInput(pw, lineNum);
				}else{
					set.clear();
					
					int max = Integer.MIN_VALUE;
					
					for(String split: splits){
						Integer val = null;
						try {
						     val = Integer.parseInt(split);
						} catch (NumberFormatException e) {
							outMsgForWrongInput(pw, lineNum);
							
							
						}
						if(val == null){
							break;
						}
						if(val<=0 ){
							outMsgForWrongInput(pw, lineNum);
							break;
						}else if(set.contains(val)){
							outMsgForWrongInput(pw, lineNum);
							break;
						}else{
							set.add(val);
							max = Math.max(max, val);
							
							
						}
					}
					if(set.size() > 0 && set.size() == splits.length){
						if(max == splits.length){
							pw.println("SUCCESS => RECEIVED: "+splits.length);
						}else{
							pw.println("FAILURE => RECEIVED: "+splits.length+", EXPECTED: "+max);
						}
					}
					
				}
			}
			
			
			lineNum+=1;
			
			oneline = br.readLine();
			
			
		}
		}catch(IOException e){
			throw new RuntimeException(e.getMessage(),e);
		}finally{
			if(pw != null){
				pw.flush();
				pw.close();
			}
			if(br!= null){
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(),e);
				}
			}
		}
		
		
    }

	private static void outMsgForWrongInput(PrintWriter pw, int lineNum) {
		pw.println("FAILURE => WRONG INPUT (LINE "+lineNum+")");
	}

	private static boolean checkAllValidDigits(String[] splits) {
		
		for(String split:splits){
			for(int i=0;i<split.length();i+=1){
				if(!Character.isDigit(split.charAt(i))){
					return false;
				}
			}
		 
		}
		return true;
	}

}




