import java.util.HashMap;
import java.util.Map;


public class PaintHouse {
	// n * 3 * 3
	// minCost(costs, costs.length, prevColor) = minCost(cost, cost.length -1, color) + paintFirst(color) for cost in r b g and color != prevColor
    public int minCost(int[][] costs){
    	return minCostRecursive(costs, costs.length, -1, new HashMap<String, Integer>());
    }
    
    private String createKey(int lengthLeft, int prevColor){
    	return lengthLeft+","+prevColor;
    }
    
    
    public int minCostIterative(int[][] costs){
    	int totalCosts[][] = new int [costs.length +1][3];
    	for(int i=1; i< totalCosts.length; i+=1){
    		for(int j=0; j<totalCosts[0].length; j+=1){
    			totalCosts[i][j] = Integer.MAX_VALUE;
    		}
    	}
    	for(int i = 1 ; i< totalCosts.length;i+=1){
    		for(int color = 0; color <=2 ; color +=1){
    			for(int prevColor = 0; prevColor <=2; prevColor +=1){
    				if(i==1 || prevColor != color){
    					totalCosts[i][color] = Math.min(totalCosts[i][color], totalCosts[i-1][prevColor] + costs[i-1][color]);
    				}
    			}
    			
    		}
    	}
    	int[] finalTotals = totalCosts[totalCosts.length-1];
    	return Math.min(finalTotals[2], Math.min(finalTotals[0], finalTotals[1]));
    }
    
 public int minCostRecursive(int[][] costs, int lengthLeft, int prevColor, Map<String, Integer> cache){
    	if(lengthLeft == 0) return 0;
    	String key = createKey(lengthLeft, prevColor);
    	if(cache.containsKey(key)){
    		return cache.get(key);
    	}
    	int minCost = Integer.MAX_VALUE;
    	for(int color = 0; color <=2; color+=1){
    		if(color != prevColor){
    			minCost = Math.min(minCost, minCostRecursive(costs, lengthLeft-1, color, cache)+ costs[costs.length - lengthLeft][color]);
    		}
    	}
    	return minCost;
    }
    
	public static void main(String[] args) {
		int[][] costs = new int[][]{{4,1,2}, {7,2,4}, {7,8,9}};
		// TODO Auto-generated method stub
		System.out.println(new PaintHouse().minCost(costs ));
		System.out.println(new PaintHouse().minCostIterative(costs ));
		

	}

}
