

/******************************************************************************
 * File: ConcreteSymbol.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An subclass of GeneralizedSymbol representing a concrete symbol.
 */
package com.keithschwarz.aic.parsing.analysis;

/**
 * A subclass of GeneralizedSymbol representing a real grammar symbol, as
 * opposed to a special symbol like &epsilon; or EOF.
 */
public final class ConcreteSymbol extends GeneralizedSymbol {
    /** The symbol being wrapped. */
    private final Symbol symbol;

    /**
     * Wraps the given symbol symbol in a new ConcreteSymbol wrapper.
     *
     * @param symbol The symbol to wrap.
     */
    public ConcreteSymbol(Symbol symbol) {
        if (symbol == null) throw new NullPointerException();

        this.symbol = symbol;
    }

    /**
     * Hands back the stored symbol symbol.
     *
     * @return The stored symbol symbol.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Computes a hash code for this object.
     *
     * @return A hash code for this object.
     */
    @Override 
    public int hashCode() {
        return symbol.hashCode();
    }

    /**
     * Returns whether this object is equal to some other object.
     *
     * @param o The other object to compare to.
     * @return Whether this object is equal to the other object.
     */
    @Override 
    public boolean equals(Object o) {
        /* We cannot be equal to it if it isn't a ConcreteSymbol. */
        if (!(o instanceof ConcreteSymbol)) return false;

        /* Downcast and check whether its symbol is equal to our symbol. */
        return symbol.equals(((ConcreteSymbol)o).symbol);
    }

    /**
     * Returns a human-readable representation of this concrete symbol.
     *
     * @return A human-readable representation of this concrete symbol.
     */
    @Override
    public String toString() {
        return symbol.toString();
    }
}
