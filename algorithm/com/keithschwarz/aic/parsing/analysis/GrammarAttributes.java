

/******************************************************************************
 * File: GrammarAttributes.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * Utility functions for computing particular attributes of grammars, such as
 * the FIRST and FOLLOW sets for the grammar.
 */
package com.keithschwarz.aic.parsing.analysis;
import java.util.*;

/**
 * Utility functions for computing particular attributes of grammars, such as
 * the FIRST and FOLLOW sets for the grammar.
 * <p>
 * These utility functions are used by the parser generators to learn enough
 * about the grammar to generate parse tables.
 */
public final class GrammarAttributes {
    /**
     * Computes and returns the FIRST sets for all of the nonterminal symbols
     * in the grammar.
     * <p>
     * Intuitively, the FIRST set of a symbol is the set of terminal symbols
     * that can appear at the start of a string derivable from that symbol.
     * The FIRST set of a nonterminal A in a grammar is the set of all terminal
     * symbols that can appear at the start of some string derivable from A.
     * Additionally, if A is nullable (it can derive the empty string), then
     * the empty string &epsilon; is also contained in FIRST.  
     * <p>
     * Given FIRST sets for all terminals and nonterminals, we can consider the
     * generalization of FIRST sets to strings of terminals and nonterminals,
     * which is defined as follows:
     * <ul>
     * <li> FIRST(&epsilon;) = &epsilon; </li>
     * <li> FIRST(xw) = x, if x is a terminal symbol.</li>
     * <li> FIRST(Aw) = FIRST(A) if A is a nonterminal and &epsilon; is not
     *      contained in FIRST(A). </li>
     * <li> FIRST(Aw) = (FIRST(A) - &epsilon;) union FIRST(w) if A is a 
     *      nonterminal and &epsilon; is contained in FIRST(A). </li>
     * </ul>
     * FIRST sets are used by the LL(1) parser to help fill in the table; 
     * specifically, the action for a combination of a nonterminal A and a 
     * terminal x is to choose a production A -&gt; w such that x is in 
     * FIRST(w).
     * <p>
     * This implementation of FIRST set computation works by using a fixed-
     * point iteration.  We begin by adding &epsilon; to the FIRST set of all
     * nonterminals that immediately derive &epsilon;, then use the logic from
     * above to iteratively update the FIRST sets of all the nonterminals.
     * This procedure never removes a terminal or &epsilon; from a FIRST set,
     * and since there are only finitely many terminals and nonterminals the
     * procedure is guaranteed to terminate.
     *
     * @param g The grammar whose FIRST sets should be computed.
     * @return A map associating nonterminals in the grammar with their FIRST
     *         sets.
     */
    public static Map<Nonterminal, Set<GeneralizedSymbol>>
        computeFirstSets(Grammar g) {
        /* Sanity-check the input. */
        if (g == null) throw new NullPointerException("g must not be null.");

        /* Create a map that will hold the result of this operation. */
        Map<Nonterminal, Set<GeneralizedSymbol>> first = 
            initializeNonterminalMapping(g);

        /* Run the fixed-point iteration.  Continuously update the FIRST sets
         * for each nonterminal by iteratively adding in symbols created by
         * each production.
         */
        boolean shouldContinue;
        do {
            /* Initially, say that we shouldn't keep repeating this process.
             * We may be proven wrong about this.
             */
            shouldContinue = false;

            /* For each production, get the symbols that should be added to the
             * FIRST set of its nonterminal, then add them in to its FIRST set.
             */
            for (Production prod: g) {
                /* See what is in the FIRST set of its production, given our
                 * current definition of the FIRST sets.
                 */
                Set<GeneralizedSymbol> computedFirst =
                    getFirstSetForSequence(prod.getProduction(), first);

                /* Add this in to the FIRST set for this nonterminal.  If this
                 * changes the FIRST set, we need to loop at least one more
                 * time.
                 */
                if (first.get(prod.getNonterminal()).addAll(computedFirst))
                    shouldContinue = true;
            }

        } while (shouldContinue);

        return first;
    }

    /**
     * Computes the FOLLOW sets for each nonterminal in a grammar.
     * <p>
     * The FOLLOW sets for a grammar are the set of symbols that can legally
     * appear immediately after some nonterminal symbol in some legal
     * sentential form.  Formally, a symbol x is in the FOLLOW set of a
     * nonterminal A if there exist strings s, r such that S =&gt;* sAxr, where
     * S is the start symbol of the grammar.
     * <p>
     * FOLLOW sets are used in LL(1) parsers to determine which action should
     * be taken when a nonterminal needs to be expanded, but that nonterminal
     * can derive the empty string.  In this case, the parser "looks past" the
     * nonterminal to see what terminals can appear in its FOLLOW set.  It then
     * decides what to do based on what it finds there.
     * <p>
     * FOLLOW sets are also computed using a fixed-point iteration that takes
     * the FIRST sets as input.  We begin by adding a special EOF marker to the
     * FOLLOW set of the start symbol, which marks that the end of the input
     * should follow a legal production of the start symbol.  From that point
     * forward, we use the following rules to update the FOLLOW sets:
     * <ul>
     * <li> If A -&gt; xBw, where B is a nonterminal, x is a string, and w is
     *      a string, we add FIRST(w) - &epsilon; to FOLLOW(B).  Intuitively,
     *      if B can be followed by some string derivable from w, then anything
     *      that starts of w can follow B.
     * <li> If A -&gt; xBw where &epsilon; is contained in FIRST(w), then we
     *      add everything from FOLLOW(A) to FOLLOW(B).  Intuitively, if A can
     *      derive a string ending with B, then anything that follows A can
     *      also follow B.  We also add in the non-epsilon symbols from
     *      FIRST(w). </li>
     * </ul>
     *
     * @param g The grammar whose FOLLOW sets should be computed.
     * @param first The FIRST sets for g.
     * @return The FOLLOW sets for g.
     */
    public static Map<Nonterminal, Set<GeneralizedSymbol>>
        computeFollowSets(Grammar g, Map<Nonterminal, Set<GeneralizedSymbol>> first) {
        /* Ensure the arguments aren't null. */
        if (g == null || first == null)
            throw new NullPointerException();
        
        /* Create a map that will hold the result. */
        Map<Nonterminal, Set<GeneralizedSymbol>> follow = 
            initializeNonterminalMapping(g);

        /* Add the EOF marker to the start symbol's FOLLOW set. */
        follow.get(g.getStartSymbol()).add(SpecialSymbols.EOF);

        /* Run the fixed-point iteration to compute FOLLOW sets. */
        boolean shouldContinue;
        do {
            shouldContinue = false;

            /* For each production, scan the production to find what
             * nonterminals, if any, it contains.  For each nonterminal, use
             * the aforementioned logic to update the FOLLOW set for that
             * nonterminal.
             */
            for (Production prod: g) {
                /* We use a standard for-loop here instead of a foreach loop
                 * because we need the index of the current symbol so that we
                 * can talk about the symbols following the current symbol.
                 */
                List<Symbol> string = prod.getProduction();
                for (int i = 0; i < string.size(); ++i) {
                    Symbol curr = string.get(i);

                    /* If this symbol is a terminal, skip over it. */
                    if (curr instanceof Terminal) continue;

                    /* Otherwise, downcast to the nonterminal that it really
                     * is.
                     */
                    Nonterminal nt = (Nonterminal) curr;

                    /* Get the FIRST set of the remainder of the string. */
                    Set<GeneralizedSymbol> startOfRest = 
                        getFirstSetForSequence(string.subList(i + 1, string.size()),
                                               first);

                    /* Add in everything from this set except epsilon. */
                    for (GeneralizedSymbol s: startOfRest) {
                        /* Skip epsilon; it never appears in a FOLLOW set. */
                        if (s.equals(SpecialSymbols.EPSILON)) continue;

                        /* Add this symbol to the FOLLOW set. */
                        if (follow.get(nt).add(s))
                            shouldContinue = true;
                    }

                    /* If this set contains epsilon, then we should also add
                     * the FOLLOW set from the nonterminal at the head of this
                     * production to the FOLLOW set.
                     */
                    if (startOfRest.contains(SpecialSymbols.EPSILON)) {
                        if (follow.get(nt).addAll(follow.get(prod.getNonterminal())))
                            shouldContinue = true;
                    }
                }
            }

        } while (shouldContinue);

        return follow;
    }

    /**
     * Given a sequence of terminals and nonterminals, along with the computed
     * FIRST sets for those nonterminals, returns the set of symbols that could
     * appear at the start of a string derivable from the initial sequence.
     *
     * @param sequence The sequence of terminals and nonterminals.
     * @param first The FIRST sets for those nonterminals.
     * @return The set of symbols that could start off a string derivable from
     *         the initial string.
     */
    public static Set<GeneralizedSymbol> 
        getFirstSetForSequence(List<Symbol> sequence,
                               Map<Nonterminal, Set<GeneralizedSymbol>> first) {
        Set<GeneralizedSymbol> result = new HashSet<GeneralizedSymbol>();

        /* Iterate across the symbols in the sequence.  For each symbol:
         *
         *   1. If it's a terminal symbol, our computation stops with that
         *      terminal added to the result set.
         *   2. If it's a nonterminal that doesn't contain epsilon in its FIRST
         *      set, our computation stops by adding all the symbols from its
         *      FIRST set.
         *   3. If it's a nonterminal that does contain epsilon in its FIRST
         *      set, our computation adds all the non-epsilon symbols from its
         *      FIRST set to our set, but then continues to the next symbol.
         */
        for (Symbol s: sequence) {
            /* If it's a terminal, we're done. */
            if (s instanceof Terminal) {
                result.add(new ConcreteSymbol((Terminal) s));
                return result;
            }

            /* Otherwise, this is a nonterminal.  For simplicity, downcast it
             * to the real type.
             */
            Nonterminal nt = (Nonterminal) s;

            /* If the FIRST set for this nonterminal does not contain epsilon,
             * add everything from its FIRST set to the result set and stop.
             */
            if (!first.get(nt).contains(SpecialSymbols.EPSILON)) {
                result.addAll(first.get(nt));
                return result;
            }

            /* Otherwise, it does contain epsilon.  Add everything in from that
             * set, but then take out the epsilon.
             */
            result.addAll(first.get(nt));
            result.remove(SpecialSymbols.EPSILON);
        }

        /* If we made it here, then there must be some way that the string can
         * derive epsilon (either the production was empty, or it's a sequence
         * of nonterminals that are all nullable.
         */
        result.add(SpecialSymbols.EPSILON);
        return result;
    }


    /**
     * Creates a map that associates each nonterminal with an empty set.
     *
     * @param g The grammar for which the set should be constructed.
     * @return A map associating each nonterminal with an empty set.
     */
    private static Map<Nonterminal, Set<GeneralizedSymbol>>
        initializeNonterminalMapping(Grammar g) {
        Map<Nonterminal, Set<GeneralizedSymbol>> result = 
            new HashMap<Nonterminal, Set<GeneralizedSymbol>>();

        /* Add a mapping for each nonterminal in the grammar. */
        for (Nonterminal nt: g.getNonterminals())
            result.put(nt, new HashSet<GeneralizedSymbol>());

        return result;
    }

}
