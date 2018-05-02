package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: SpecialSymbols.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * Constants representing special terminal symbols that cannot normally appear
 * in a grammar but which may need to appear in special contexts (such as FIRST
 * or FOLLOW sets).
 */

/**
 * Constants representing special symbols that cannot normally appear in a
 * grammar but which may appear in similar contexts.
 */
public final class SpecialSymbols extends GeneralizedSymbol {
    /** A special symbol representing the empty string. */
    public static SpecialSymbols EPSILON = new SpecialSymbols("\u03B5");

    /** A special symbol representing the end of input. */
    public static SpecialSymbols EOF     = new SpecialSymbols("$");

    /** The name of this special symbol. */
    private final String name;

    /**
     * Constructor allocates a new special symbol with the given name.
     *
     * @param name The name of the special symbol.
     */
    private SpecialSymbols(String name) {
        this.name = name;
    }

    /**
     * Returns a human-readable representation of this special symbol.
     *
     * @return A human-readable representation of this special symbol.
     */
    @Override 
    public String toString() {
        return name.toString();
    }
}
