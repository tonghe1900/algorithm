package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: Terminal.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a terminal symbol in a grammar.
 */

/**
 * A class representing a terminal symbol in a grammar.
 */
public final class Terminal extends Symbol {
    /**
     * Creates a new terminal symbol with the given name, which must not be
     * null or the empty string.
     *
     * @param name The name of the terminal.
     */
    public Terminal(String name) {
        super(name, true);
    }
}
