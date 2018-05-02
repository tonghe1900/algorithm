package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: ShiftAction.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An action class representing that the parser should shift.
 */


/**
 * An action class representing that the parser should shift.
 * <p>
 * Since all shift actions are the same (the only difference is where the shift
 * should take the parser), this is an enumerated type with one instance.
 */
public enum ShiftAction implements LRAction {
    INSTANCE;


    /**
     * Returns a human-readable representation of this shift action.
     *
     * @return A human-readable representation of this shift action.
     */
    @Override
    public String toString() {
        return "Shift";
    }
}
