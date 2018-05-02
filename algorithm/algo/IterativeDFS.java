package algo;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Stack;

public class IterativeDFS {
	private boolean[] marked;
	private int[] edgeTo;
    public void dfs(Graph g, int s){
    	int vertexNum = g.V();
    	marked = new boolean[vertexNum];
    	edgeTo = new int[vertexNum];
    	Stack<Integer> stack = new Stack<>();
    	stack.push(s);
    	while(!stack.isEmpty()){
    		int v = stack.pop();
    		if(!marked[v]){
    			marked[v] = true;
    			for(int adj : g.adj(v)){
    				if(!marked[adj]){
    					edgeTo[adj] = v;
    					stack.push(adj);
    				}
    			}
    		}
    	}
    }
}
