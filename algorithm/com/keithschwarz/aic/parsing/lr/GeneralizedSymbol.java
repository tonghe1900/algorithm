package com.keithschwarz.aic.parsing.lr;
/******************************************************************************
 * File: GeneralizedSymbol.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a generalization of a symbol that might appear in a
 * FIRST or FOLLOW set.  In addition to the normal grammar symbols that might
 * appear in these sets, we need to be able to handle epsilon and EOF markers.
 * To give a type-safe, unified treatment of these types, we introduce an extra
 * level of indirection.
 */


/**
 * A class representing a generalization of a symbol that might appear in a
 * FIRST or FOLLOW set.  In addition to the normal grammar symbols that might
 * appear in these sets, we need to be able to handle epsilon and EOF markers.
 * To give a type-safe, unified treatment of these types, we introduce an extra
 * level of indirection.
 */
public abstract class GeneralizedSymbol {

}