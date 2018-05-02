import java.util.HashMap;
import java.util.Map;

/**
 * Problem Statement for MiniPaint


Problem Statement
    	You have been given a String[] picture. Each character in picture represents a space in the picture. A 'B' designates a space that needs to be painted black, and a 'W' denotes a space that must be painted white. The painting device you have been given only makes horizontal strokes, of any length, exactly one space high. In addition, it can only use 1 color at a time. Due to the nature of the paint, a space cannot be painted twice. For example, the following picture could be colored in 6 distinct strokes:
picture = {"BBBBBBBBBBBBBBB",
           "WWWWWWWWWWWWWWW",
	   "WWWWWWWWWWWWWWW",
           "WWWWWBBBBBWWWWW"}
You would use 1 stroke for each of the first 3 lines, and then 3 strokes on the last line.



This wouldn't be an issue if we had forever to paint the picture. Unfortunately, you only have enough time to make at most maxStrokes distinct strokes. Any more strokes would put you past your deadline. Since finishing on time is more important than getting it perfect, you are willing to mispaint some of the spaces. Return the fewest number of spaces that can be mispainted while still using no more than maxStrokes strokes. An unpainted space is considered mispainted.
 
Definition
    	
Class:	MiniPaint
Method:	leastBad
Parameters:	String[], int
Returns:	int
Method signature:	int leastBad(String[] picture, int maxStrokes)
(be sure your method is public)
    
 
Constraints
-	picture will contain between 1 and 50 elements inclusive.
-	Each element of picture will contain between 1 and 50 characters inclusive.
-	Each element of picture will contain the same number of characters.
-	Each character in each element of picture will be (quotes for clarity) 'B' or 'W'.
-	maxStrokes will be between 0 and 3000 inclusive.
 
Examples
0)	
    	
{
"BBBBBBBBBBBBBBB",
"WWWWWWWWWWWWWWW",
"WWWWWWWWWWWWWWW",
"WWWWWBBBBBWWWWW"}
6
Returns: 0
Exactly enough strokes to finish the job.
1)	
    	
{
"BBBBBBBBBBBBBBB",
"WWWWWWWWWWWWWWW",
"WWWWWWWWWWWWWWW",
"WWWWWBBBBBWWWWW"}
4
Returns: 5
One stroke for each row produces the least problem.
2)	
    	
{
"BBBBBBBBBBBBBBB",
"WWWWWWWWWWWWWWW",
"WWWWWWWWWWWWWWW",
"WWWWWBBBBBWWWWW"}
0
Returns: 60
Now the entire picture will be mispainted.
3)	
    	
{
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW",
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW",
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW",
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW",
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW",
"BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW"}
100
Returns: 40
This one needs a lot of strokes.
4)	
    	
{"B"}
1
Returns: 0

 * @author het
 *
 */
public class MiniPaint {
	// time complexity    height* maxStroker* maxstroker* length* maxstroker* length*length
	//  leastBad(lines, maxStroker) = leastBadOneline(lines[0], strokersUsedByFirstLine) + leastBad(lines -1, maxStroker - strokersUsedByFirstLine)
	//for strokersUsedByFirstLine in 0 .. maxStroker
	//leastBadOneline(lines[0].length, strokersUsedByFirstLine) =  oneStroke(oneStrokelength) + leastBadOneline(lines[0].length-oneStrokelength, strokersUsedByFirstLine-1)
	
	public int leastBad(String[] picture, int maxStrokes){
		if(picture == null || picture.length == 0 ) return 0;
		
		int result = leastBadHelper(picture, picture.length, maxStrokes, new HashMap<String, Integer>());
		return result;
	}
    private String createKey(int height, int maxStrokes){
    	return height+","+ maxStrokes;
    }
	private int leastBadHelper(String[] picture, int height, int maxStrokes, Map<String, Integer> cache) {
		if(height == 0) return 0;
		if(maxStrokes <= 0) return height* picture[0].length();
		String key = createKey(height, maxStrokes);
		if(cache.containsKey(key)){
			return cache.get(key);
		}
		int strokeNum= Integer.MAX_VALUE;
		for(int i=0; i<=maxStrokes;i+=1){
			strokeNum = Math.min(strokeNum, leastBadHelper(picture, height-1, maxStrokes - i, cache) + leastBadForOneLine(picture[picture.length - height], i));
		
		}
		cache.put(key, strokeNum);
		return strokeNum;
	}
	
	private int leastBadForOneLine(String line, int maxStrokes){
		int result = leastBadForOneLineHelper(line, line.length(), maxStrokes, new HashMap<String, Integer>());
		return result;
	}

	private int leastBadForOneLineHelper(String line, int length,
			int maxStrokes, HashMap<String, Integer> cache) {
		if(length == 0) return 0;
		if(maxStrokes <= 0) return length;
		String key = createKey(length, maxStrokes);
		if(cache.containsKey(key)){
			return cache.get(key);
		}
		int strokeNum= Integer.MAX_VALUE;
		for(int i=1;i<=length;i+=1){
			strokeNum = Math.min(strokeNum, leastBadForOneLineHelper(line, length - i, maxStrokes -1, cache)+ badSpacePaint(line, line.length() - length, i));
		}
		cache.put(key, strokeNum);
		return strokeNum;
	}
	private int badSpacePaint(String line, int startIndex, int oneStrokeLength) {
		int endIndex = startIndex + oneStrokeLength -1;
		int blackNums = 0;
		for(int i = startIndex; i<= endIndex; i+=1){
			if('B' == line.charAt(i)){
				blackNums +=1;
			}
		}
		return Math.min(blackNums, oneStrokeLength - blackNums);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("BBBBBBBBBBBBBBB".length());
		String[] picture=
		{
			"BBBBBBBBBBBBBBB",
			"WWWWWWWWWWWWWWW",
			"WWWWWWWWWWWWWWW",
			"WWWWWBBBBBWWWWW"};
		int maxStrokes = 4;
		System.out.println(new MiniPaint().leastBad(picture, maxStrokes));
		
		 String []picture1= {
			"B"};
		 maxStrokes = 0;
		 System.out.println(new MiniPaint().leastBad(picture1, maxStrokes));

	}

}
