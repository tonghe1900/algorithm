

/******************************************************************************
 * File: ParseTree.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a parse tree showing a derivation of some string in a
 * grammar.
 */
package com.keithschwarz.aic.parsing.analysis;
import java.util.*;

/**
 * A class representing a parse tree showing a derivation of some string in the
 * grammar.  Each tree node holds a symbol (either terminal or nonterminal)
 * along with an ordered list of child nodes.
 *
 * @author Keith Schwarz (htiek@cs.stanford.edu)
 */
public class ParseTree implements Iterable<ParseTree> {
    /** The grammar symbol represented by this node of the tree. */
    private final Symbol symbol;

    /** The children of this parse tree node, in the order in which they appear */
    private final List<ParseTree> children;

    /**
     * Constructs a new parse tree wrapping the given symbol with the given
     * children.
     *
     * @param symbol The symbol at this parse tree node.
     * @param children The children of this parse tree node.
     */
    public ParseTree(Symbol symbol, List<ParseTree> children) {
        if (symbol == null || children == null)
            throw new NullPointerException();

        this.symbol = symbol;
        this.children = children;
    }

    /**
     * Constructs a new parse tree node holding the given symbol, but with
     * no children.
     *
     * @param symbol The symbol held by this parse tree node.
     */
    public ParseTree(Symbol symbol) {
        this(symbol, new ArrayList<ParseTree>());
    }

    /**
     * Returns the symbol held by this parse tree node.
     *
     * @return The symbol held by this parse tree node.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Returns a mutable view of the children of this parse tree node.
     *
     * @return A mutable view of the children of this parse tree node.
     */
    public List<ParseTree> getChildren() {
        return children;
    }

    /**
     * Returns a mutable iterator to traverse the children of this parse tree.
     *
     * @return A mutable iterator traversing the children of this node.
     */
    public Iterator<ParseTree> iterator() {
        return getChildren().iterator();
    }

    /**
     * Returns a human-readable representation of the parse tree.
     *
     * @return A human-readable representation of the parse tree.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(symbol);
        if (symbol instanceof Nonterminal) {
            builder.append(" -> ");
            builder.append(getChildren());
        }
        return builder.toString();
    }
}
