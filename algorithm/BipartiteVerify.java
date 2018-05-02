/*****************************************************************************
 * File: BipartiteVerify.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A function for determining whether or not a given graph is bipartite.  The
 * algorithm works by using a rather famous result about bipartite graphs:
 *
 *          A graph G is bipartite iff it contains no odd cycles.
 *
 * We can prove this claim as follows.  To prove the forward direction, we
 * prove the contrapositive; that if G contains an odd cycle, it cannot be
 * bipartite.  The proof is by contradiction.  Suppose that G is bipartite;
 * then there is a way of partitioning the nodes in G into two groups A and B
 * such that there is no edge between nods in A or nodes in B.  Now, let
 * C = (v0, v1, v2, ..., v_{2n}, v0) be the odd cycle in G and assume without
 * loss of generality that v0 \in A.  This means that v1 \in B (since
 * otherwise there would be an edge between two nodes in A), and so v2 \in A,
 * etc.  In general, this means that v_{2i} \in A and v_{2i+1} \in B.  But
 * there is an edge from v0 to v_{2n}, both of which are in A, contradicting
 * that there are no edges between nodes in A or nodes in B, and so G is not
 * bipartite.
 *
 * To prove the other direction of the claim, that if G has no odd cycles it is
 * bipartite, we similarly prove the contrapositive - that if G is not
 * bipartite, it contains an odd cycle.  Consider any non-bipartite graph G and
 * let F be any spanning forest of G.  For each tree T in F, pick an arbitrary
 * node of the tree to use as the root, then label each node with its distance
 * from the root.  Some nodes will have odd depth; others will have even depth.
 * Now, partition the nodes into nodes of odd depth and nodes of even depth.
 * Since the graph is not bipartite, there must be at least one edge between
 * nodes of even depth or of odd depth; call it e = (u, v).  Now consider the
 * least common ancestor w of u and v in the tree T.  Then the parity of the
 * lengths of the paths from u to w and from w to v in T must be the same,
 * since each path is either a path between two even nodes, two odd nodes, or
 * an even node and an odd node.  This means that the length of the path from
 * u to w and then from w to v in tree T is even.  But this gives us our odd
 * cycle - we can take this path, then cross edge e = (u, v) to end up back
 * where we started after following an odd number of edges.
 *
 * The algorithm implemented here works by choosing as its spanning forest a
 * depth-first search tree in the graph G.  As it explores nodes in a DFS
 * fashion, it marks the parity of the distance of each node from the root of
 * the particular tree it resides in.  We can then check every edge and see if
 * any runs between two nodes of even parity or of odd parity to check whether
 * the odd cycle exists.  As an optimization, though, we combine this logic
 * with the logic to actually perform the depth-first search.  Whenever we
 * expand a node for the first time, we mark its parity, then explore all
 * outgoing arcs with the assumption that the endpoints of those arcs are at a
 * different parity than the current node.  If we ever explore an arc where
 * both nodes are known to have the same parity, we have found an arc between
 * two nodes of the same parity and have detected an odd cycle.  Since the DFS
 * considers each arc in the graph twice (once in each direction), we're
 * guaranteed that we will locate such an edge if it exists.
 *
 * The runtime of this algorithm is surprisingly good - it takes a single DFS,
 * which runs in linear (O(|V| + |E|)) time.
 */
import java.util.*; // For HashMap

public final class BipartiteVerify {
    /**
     * Returns whether the input graph is bipartite.
     *
     * @param g The graph to examine.
     * @return Whether the graph is bipartite.
     */
    public static <T> boolean isBipartite(UndirectedGraph<T> g) {
        /* We'll associate each node in the graph with its parity in the DFS
         * search.  This map will hold this association.
         */
        Map<T, Boolean> parityTable = new HashMap<T, Boolean>();
        
        /* Start off a DFS from each node in the graph, unless we've already
         * visited it.  We hoist the check for whether the node has been
         * explored before out of the recursion, since if the node has been
         * explored previously we don't care what parity it has.
         */
        for (T node: g)
            if (!parityTable.containsKey(node) && 
                !dfsExplore(node, g, parityTable, true))
                return false;

        /* If we got here, it means that the graph contains no odd cycles and
         * is therefore bipartite.
         */
        return true;
    }

    /**
     * Recursively explores outward from the given node in the DFS, labeling
     * it with the appropriate parity.  If a contradiction is detected in the
     * parity assignment, this returns false.
     *
     * @param node The node to explore.
     * @param g The graph in which to perform the DFS.
     * @param parityTable A map from nodes to their parities.
     * @param parity The expected parity of this node.
     */
    private static <T> boolean dfsExplore(T node, UndirectedGraph<T> g,
                                          Map<T, Boolean> parityTable,
                                          boolean parity) {
        /* If we've visited this node, return whether the parity matches the
         * expected parity.
         */
        if (parityTable.containsKey(node))
            return parityTable.get(node).equals(parity);

        /* Otherwise, mark that this node has the indicated parity. */
        parityTable.put(node, parity);

        /* Continue exploring outward from this node.  If we find a 
         * contradiction, immediately abort and report failure.
         */
        for (T endpoint: g.edgesFrom(node))
            /* Note that we use the opposite parity for all child nodes, since
             * we alternate between odd and even in the DFS tree.
             */
            if (!dfsExplore(endpoint, g, parityTable, !parity))
                return false;

        /* If we made it here, everything in this subtree worked out. */
        return true;
    }
}
