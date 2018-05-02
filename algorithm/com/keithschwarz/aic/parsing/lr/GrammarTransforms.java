package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: GrammarTransforms.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * Utility functions for applying mechanical transformations to grammars.
 */





import java.util.*;



/**
 * Utility functions for applying mechanical transformations to grammars.
 */
public final class GrammarTransforms {
    /* This class is not meant to be instantiated. */
    private GrammarTransforms() {
        // Empty //
    }

    /**
     * Augments a grammar by introducing a new start symbol.
     * <p>
     * Many parsing algorithms (especially the LR methods) require the grammar
     * to be transformed by adding a special start symbol that cannot otherwise
     * appear in the grammar.  This function accepts as input a grammar and
     * then produces a new augmented grammar that contains an extra nonterminal
     * as the start symbol.  This nonterminal is guaranteed not to appear
     * anywhere else in the grammar.
     *
     * @param g The grammar to augment.
     * @return An augmented grammar with a new start symbol.
     */
    public static Grammar augmentGrammar(Grammar g) {
        /* We need to find a name for a nonterminal that isn't currently being
         * used by the grammar.  To do this, we'll get all of the names being
         * used, sort them, and then append a marker onto the end of the
         * alphabetically last of the names.
         */
        SortedSet<String> names = new TreeSet<String>();
        for (Nonterminal nt: g.getNonterminals())
            names.add(nt.getName());

        /* Make our new nonterminal. */
        Nonterminal start = new Nonterminal(names.last() + "_(start)");

        /* Get the list of old productions and add this production into it. */
        List<Production> productions = new ArrayList<Production>(g.getProductions());
        productions.add(new Production(start, Arrays.asList(new Symbol[]{g.getStartSymbol()})));

        /* Construct a new grammar from this one. */
        return new Grammar(productions, start);
    }
}
