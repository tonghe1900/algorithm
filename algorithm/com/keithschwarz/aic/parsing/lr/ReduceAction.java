package com.keithschwarz.aic.parsing.lr;


/******************************************************************************
 * File: ReduceAction.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An action class indicating that the parser should apply a reduction.
 */


/**
 * An action class indicating that the parser should apply a reduction.
 * <p>
 * When the parser needs to apply a reduction, it has to know what reduction
 * to apply.  This class thus encapsulates the information about what
 * production should be reversed.
 */
public final class ReduceAction implements LRAction {
    /** The production that should be reversed. */
    private final Production production;

    /**
     * Creates a new ReduceAction that wraps the given production.
     *
     * @param production The production that should be wrapped.
     */
    public ReduceAction(Production production) {
        if (production == null)
            throw new NullPointerException();
        this.production = production;
    }

    /**
     * Returns the production associated with this reduce action.
     *
     * @return The production that should be reversed.
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Returns whether this reduce action is equal to some other object.
     *
     * @return Whether this reduce action is equal to some other object.
     */
    @Override 
    public boolean equals(Object o) {
        /* See if the other object is a ReduceAction and fail if it isn't. */
        if (!(o instanceof ReduceAction))
            return false;

        /* Downcast to a ReduceAction and confirm that the productions match. */
        ReduceAction other = (ReduceAction)o;
        return production.equals(other.production);
    }

    /**
     * Returns a hash code for this reduce action.
     *
     * @return A hash code for this reduce action.
     */
    @Override
    public int hashCode() {
        return production.hashCode();
    }

    /**
     * Returns a human-readable representation of this reduce action.
     *
     * @return A human-readable representation of this reduce action.
     */
    @Override
    public String toString() {
        return "Reduce " + production;
    }
}
