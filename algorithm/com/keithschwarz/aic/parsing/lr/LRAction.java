package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: LRAction.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An interface representing some action that may be taken by an LR parser.
 */


/**
 * An interface representing some action that may be taken by an LR parser.
 * <p>
 * LR parsers are shift/reduce parsers that work by applying various operations
 * on a parsing stack.  This interface provides a slightly type-safe way for
 * parsers to record what those actions are.
 */
public interface LRAction {
    /* Empty; this is intended to used to exclude spurious actions. */
}
