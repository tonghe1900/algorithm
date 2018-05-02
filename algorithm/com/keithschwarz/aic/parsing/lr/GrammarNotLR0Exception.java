package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: GrammarNotLR0Exception.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An exception class representing an error condition where an LR(0) parser
 * generator was asked to construct a parser for a grammar that is not LR(0).
 */

/**
 * An exception class that is thrown if a gramar that is not LR(0) is fed into
 * an LR(0) parser generator.
 */
public class GrammarNotLR0Exception extends Exception {
    /**
     * Constructs a new GrammarNotLR0Exception exception with the given error
     * message.
     *
     * @param message The error message.
     */
    public GrammarNotLR0Exception(String message) {
        super(message);
    }
}
