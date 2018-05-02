
/******************************************************************************
 * File: GrammarNotLL1Exception.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An exception class that is thrown if a gramar that is not LL(1) is fed into
 * an LL(1) parser generator.
 */
package com.keithschwarz.aic.parsing.analysis;

/**
 * An exception class that is thrown if a gramar that is not LL(1) is fed into
 * an LL(1) parser generator.
 */
public class GrammarNotLL1Exception extends Exception {
    /**
     * Constructs a new GrammarNotLL1Exception exception with the given error
     * message.
     *
     * @param message The error message.
     */
    public GrammarNotLL1Exception(String message) {
        super(message);
    }
}
