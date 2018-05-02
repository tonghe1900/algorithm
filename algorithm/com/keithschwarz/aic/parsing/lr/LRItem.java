package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: LRItem.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing an LR item, which is a production annotated with some
 * position within that production along with a lookahead.
 */

import com.keithschwarz.aic.parsing.analysis.*;
import java.util.*;

/**
 * A class representing an LR item.
 * <p>
 * An LR item is a production A &rarr; wx annotated with two extra pieces of
 * information: a <i>position</i>, indicating how much of the production we
 * have matched so far, and a <i>lookahead</i>, indicating what characters we
 * would expect to find after this production.  We represent an LR item using
 * the syntax A &rarr;w.x [y] to mean "the production is A &rarr; wx, of which
 * we have so far matched w and expect to find y as the lookahead."
 * <p>
 * LR items are used in several parsing algorithms, including LR(0), SLR(1),
 * LALR(1), LR(1), GLR(0), and Earley.  These algorithms are all bottom-up
 * algorithms with some top-down recognition component to them.  Each works by
 * trying to reverse the steps in a derivation to arrive back at the start
 * symbol.  Since the algorithms work bottom-up, at any point they may be
 * looking at a partially-matched production.  The LR item encodes how much has
 * been matched, how much has not been matched, and what to expect afterwards.
 * This last information is important, since the LR parser may decide not to
 * apply the reduction if the lookahead symbols don't match the expected
 * lookahead.
 * <p>
 * While more general LR items must maintain both a dot lookahead and an item
 * lookahead, for simplicity our LR items will have either 0 or 1 lookahead
 * token, depending on whether this is an LR(0) item or an SLR/LR/LALR(1) item.
 */
public final class LRItem {
    /** The production associated with this item. */
    private final Production production;

    /**
     * Our index in the production, specified as the index of the symbol we are
     * just before.
     */
    private final int index;

    /** Our expected lookahead. */
    private final GeneralizedSymbol lookahead;

    /**
     * Constructs a new LR item given the specified production, index, and
     * lookahead.
     *
     * @param production The production we are in the middle of.
     * @param index The index into that production.
     * @param lookahead The lookahead for this LR item.
     */
    public LRItem(Production production, int index, GeneralizedSymbol lookahead) {
        /* Sanity-check the input. */
        if (production == null) throw new NullPointerException();
        if (index < 0 || index > production.getProduction().size())
            throw new IndexOutOfBoundsException();

        this.production = production;
        this.index = index;
        this.lookahead = lookahead;
    }

    /**
     * Hands back the index into the production.
     *
     * @return The index into the production.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Hands back the production associated with this LR item.
     *
     * @return The production associated with this LR item.
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Hands back the lookahead associated with this LR item.
     *
     * @return The lookahead associated with this LR item.
     */
    public GeneralizedSymbol getLookahead() {
        return lookahead;
    }

    /**
     * Hands back the symbol just after the dot, or null if it is not
     * defined.
     *
     * @return The symbol just after the dot, or null if there is none.
     */
    public Symbol getSymbolAfterDot() {
        return isComplete()? null : production.getProduction().get(index);
    }
    
    /**
     * Returns a new LR item corresponding to the item formed by moving this
     * item's dot forward one step.  If the dot is already at the end, this
     * throws a RuntimeException.
     *
     * @return An LR item formed by moving this LR item's dot forward one step.
     */
    public LRItem withDotMovedForward() {
        /* If we can't move the dot forward, then report an error. */
        if (isComplete())
            throw new RuntimeException("Dot at end of production " + this);

        return new LRItem(production, index + 1, lookahead);
    }

    /**
     * Returns whether this item corresponds to a production that has been
     * read in completely.
     *
     * @return Whether the dot is at the end of the production.
     */
    public boolean isComplete() {
        return index == production.getProduction().size();
    }

    /**
     * Returns whether this LRItem is equal to some other object.
     *
     * @param o The object to compare this object to.
     * @return Whether this object is equal to o.
     */
    @Override 
    public boolean equals(Object o) {
        /* Confirm that the other object is an LRItem and fail if it isn't. */
        if (!(o instanceof LRItem)) return false;

        /* Downcast to the actual LRItem, then check that this object is equal
         * to it.
         */
        LRItem other = (LRItem) o;
        if (index != other.index ||
            !production.equals(other.production))
            return false;

        /* Because lookaheads can be null, we have to be more careful with how
         * we check if they're equal.
         */
        if ((lookahead == null) != (other.lookahead == null))
            return false;
        if (lookahead == null)
            return true;
        return lookahead.equals(other.lookahead);
    }

    /**
     * Returns a hash code for this LRItem.
     *
     * @return A hash code for this LRItem.
     */
    @Override 
    public int hashCode() {
        int result = index;
        result = result * 31 + production.hashCode();
        result = result * 31 + (lookahead == null? 0 : lookahead.hashCode());
        return result;
    }

    /**
     * Returns a human-readable representation of this LR item.
     *
     * @return A human-readable representation of this LR item.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(production.getNonterminal());
        builder.append(" -> ");

        List<Symbol> string = production.getProduction();
        for (int i = 0; i < string.size(); ++i) {
            /* Put a dot here if the item is just before this character. */
            if (index == i) builder.append(".");
            builder.append(string.get(i));
            builder.append(" ");
        }

        /* If the index is just after the production, place a dot here. */
        if (index == string.size())
            builder.append(".");

        /* If we have a lookahead, append that as well. */
        if (lookahead != null) {
            builder.append("  [");
            builder.append(lookahead);
            builder.append("]");
        }

        return builder.toString();
    }
}
