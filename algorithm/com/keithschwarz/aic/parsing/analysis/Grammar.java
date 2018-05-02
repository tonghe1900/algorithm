

/******************************************************************************
 * File: Grammar.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a context-free grammar.
 */

package com.keithschwarz.aic.parsing.analysis;
import java.util.*;

/**
 * A class representing a context-free grammar.
 */
public final class Grammar implements Iterable<Production> {
    /** A collection of the productions in the grammar. */
    private final Collection<Production> productions;

    /** A collection of the nonterminals in the grammar. */
    private final Set<Nonterminal> nonterminals = new HashSet<Nonterminal>();

    /** A collection of all the terminals in the grammar. */
    private final Set<Terminal> terminals = new HashSet<Terminal>();

    /** The start symbol. */
    private final Nonterminal start;

    /**
     * Constructs a new grammar from the given productions and start symbol.
     *
     * @param productions The productions in the grammar.
     * @param start The start symbol of the grammar.
     */
    public Grammar(Collection<Production> productions, Nonterminal start) {
        if (productions == null || start == null)
            throw new NullPointerException();

        this.productions = productions;
        this.start = start;

        /* Fill in the list of terminals and nonterminals. */
        for (Production p: productions) {
            nonterminals.add(p.getNonterminal());
            for (Symbol s: p.getProduction()) {
                if (s instanceof Nonterminal)
                    nonterminals.add((Nonterminal) s);
                else // s instanceof Terminal
                    terminals.add((Terminal) s);
            }
        }

        /* Confirm that the start symbol is a nonterminal in the grammar. */
        nonterminals.add(start);
    }

    /**
     * Returns the start symbol of the grammar.
     *
     * @return The start symbol of the grammar.
     */
    public Nonterminal getStartSymbol() {
        return start;
    }

    /**
     * Returns an immutable view of the productions in the grammar.
     *
     * @return An immutable view of the productions in the grammar.
     */
    public Collection<Production> getProductions() {
        return Collections.unmodifiableCollection(productions);
    }

    /**
     * Returns an immutable iterator over the productions in the grammar.
     *
     * @return An immutable iterator over the productions in the grammar.
     */
    public Iterator<Production> iterator() {
        return getProductions().iterator();
    }

    /**
     * Returns an immutable view of the terminals in the grammar.
     *
     * @return An immutable view of the terminals in the grammar.
     */
    public Collection<Terminal> getTerminals() {
        return Collections.unmodifiableCollection(terminals);
    }

    /**
     * Returns an immutable view of the nonterminals in the grammar.
     *
     * @return An immutable view of the nonterminals in the grammar.
     */
    public Collection<Nonterminal> getNonterminals() {
        return Collections.unmodifiableCollection(nonterminals);
    }

    /**
     * Returns a human-readable description of the grammar.
     *
     * @return A human-readable description of the grammar.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Production p: this) {
            builder.append(p.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
