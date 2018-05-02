
/******************************************************************************
 * File: Nonterminal.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a nonterminal symbol in a grammar.  We introduce this
 * class so that we can have a strong type check that a CFG is well-formed, but
 * there is no other extra code.
 */
package com.keithschwarz.aic.parsing.analysis;

/**
 * A class representing a nonterminal symbol in a grammar.
 */
public final class Nonterminal extends Symbol {
    /**
     * Creates a new nonterminal symbol with the given name, which must not be
     * null or the empty string.
     *
     * @param name The name of the nonterminal.
     */
    public Nonterminal(String name) {
        super(name, false);
    }
}
