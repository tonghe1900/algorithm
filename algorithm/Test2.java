/**
 * Problem Statement
    	The latest version of your favorite adventure game has just been released. On each level you search for stars that earn you points. Simply moving over a location containing stars allows you to acquire them. To help you on your journey, you are given an overhead map of the level in a String[]. Each character in level describes the number of stars at that location. You begin in the upper left spot of the map (character 0 of element 0 of level). On the current stage you must move according to the following rules:
1) On the first pass you may only move downward or rightward each move (not diagonally) until you reach the lower right corner.
2) The second pass begins in the lower right corner where the first pass ended, and proceeds back to the beginning using only upward and leftward steps (not diagonal).
3) The final pass, like the first pass, begins in the upper left corner and proceeds to the lower right corner using only rightward and downward (not diagonal) steps.
Once the stars on a spot are claimed, they cannot be claimed again on a future pass. Return the largest possible number of stars that can be acquired.
 
Definition
    	
Class:	StarAdventure
Method:	mostStars
Parameters:	String[]
Returns:	int
Method signature:	int mostStars(String[] level)
(be sure your method is public)
    
 
Constraints
-	level will contain between 2 and 50 elements inclusive.
-	Each element of level will contain between 2 and 50 characters inclusive.
-	Each element of level will contain the same number of characters.
-	Each character in each element of level will be a digit ('0' - '9').
-	Character 0 in element 0 of level will be '0'.
 
Examples
0)	
    	
{"01",
 "11"}
Returns: 3
1)	
    	
{"0999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"
,"9999999999"}
Returns: 450
2)	
    	
{"012"
,"012"
,"012"
,"012"
,"012"
,"012"
,"012"}
Returns: 21
3)	
    	
{"0123456789",
 "1123456789",
 "2223456789",
 "3333456789",
 "4444456789",
 "5555556789",
 "6666666789",
 "7777777789",
 "8888888889",
 "9999999999"}
Returns: 335
 * @author het
 *
 */
public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
