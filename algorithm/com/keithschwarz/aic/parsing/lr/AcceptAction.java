package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: AcceptAction.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An action class representing that the parser should accept its input.
 */


/**
 * An action class representing that the parser should accept.
 * <p>
 * Since all accept actions are the same this is an enumerated type with one 
 * instance.
 */
public enum AcceptAction implements LRAction {
    INSTANCE;

    /**
     * Returns a human-readable representation of this accept action.
     *
     * @return A human-readable representation of this accept action.
     */
    @Override
    public String toString() {
        return "Accept";
    }
}
