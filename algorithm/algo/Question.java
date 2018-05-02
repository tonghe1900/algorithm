package algo;

import java.util.Arrays;
// time complexity : O(n)  n is the size of the input
// space complexity O(n)  because the result may be as big as the size of the input. Making a copy for the result array has a lot of advantages and it follows the style of functional programming: data in and data out and no side effects. 
public class Question {
	
	public static int [] findLargestSum(int [] input){
		if(input == null || input.length == 0) return new int[]{};
		if(input.length == 1) return input;
		
		int previousLargestSum = input[0];
		
		int previousStartIndex = 0;
		int largestSum = previousLargestSum;
		int largestSumStartIndex = previousStartIndex;
		int largestSumEndIndex = previousStartIndex;
		for(int i=1; i< input.length; i+=1){
			if(previousLargestSum > 0) {
				previousLargestSum += input[i];
				
			}else{
				previousLargestSum = input[i];
				previousStartIndex = i;
			}
			if(previousLargestSum > largestSum){
				largestSum = previousLargestSum;
				largestSumStartIndex = previousStartIndex;
				largestSumEndIndex = i;
			}
		}
		return Arrays.copyOfRange(input, largestSumStartIndex, largestSumEndIndex+1);
	}
    private static void printArray(int [] input){
    	for(int element: input){
    		System.out.print(element + " ");
    	}
    	System.out.println();
    }
	public static void main(String[] args) {
		printArray(findLargestSum(new int []{}));
		printArray(findLargestSum(null));
		printArray(findLargestSum(new int []{-1}));
		printArray(findLargestSum(new int []{5}));
		printArray(findLargestSum(new int []{1, -3, 1, 2, 4}));
		printArray(findLargestSum(new int []{4, -3, 3, 2, 4}));
		printArray(findLargestSum(new int []{1, 3, 1, 2, 4}));
		printArray(findLargestSum(new int []{-1, -3, -1, -2, -4}));
		printArray(findLargestSum(new int []{-1, -3, 0, -2, -4}));
		printArray(findLargestSum(new int []{-9, -3, -5, -6, -4}));
		printArray(findLargestSum(new int []{-1, -3, 4, -2, -4}));
		printArray(findLargestSum(new int []{-1, -3, 4, -2, 5}));
		printArray(findLargestSum(new int []{-1, -3, 4, -5, 5}));
		printArray(findLargestSum(new int []{-1, -3, 4, -4, 5}));
		printArray(findLargestSum(new int []{-1, -3, 4, -3, 5}));

	}

}
