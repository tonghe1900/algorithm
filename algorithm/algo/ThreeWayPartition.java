package algo;

public class ThreeWayPartition {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//[4,3,4,1,2,3,1,2], and low = 2 and high = 3
		int[] nums = new int[]{8,9,10,1,4,3,4,1,2,3,1,2, 1,1,1,0,0,0,0,5,5,5,2,3,3,2,3,2, 8,9};
		
		partition2(nums, 2, 3);
		for(int num:nums){
			System.out.print(num + ",");
		}
		System.out.println();
		

	}
	
	
	public static void partition2(int[] nums, int low, int high) {
        // Write your code here
        
        int lt = 0;
        int gt = nums.length - 1;
        int i = 0;
        while (i <= gt) {
            if (nums[i] < low) {
                exch(nums, lt++, i);
            }
            else if (nums[i] > high) {
                exch(nums, i, gt--);
            }
            else {
                i++;
            }
        }
    }
    
    public static void exch(int[] nums, int x, int y) {
        int tmp = nums[x];
        nums[x] = nums[y];
        nums[y] = tmp;
    }


}
