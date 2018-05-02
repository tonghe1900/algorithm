

/******************************************************************************
 * File: ParseErrorException.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An exception class representing that a parse error has occurred.
 */

package com.keithschwarz.aic.parsing.analysis;

/**
 * An exception class representing that a parse error has occurred.
 */
public class ParseErrorException extends Exception {
    /**
     * Constructs a ParseErrorException with the given message and cause.
     *
     * @param message A message describing the error.
     * @param cause The root cause of the exception.
     */
    public ParseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a ParseErrorException with the given cause.
     *
     * @param cause The root cause of the exception.
     */
    public ParseErrorException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a ParseErrorException with the given message.
     *
     * @param message A message describing the error.
     */
    public ParseErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a ParseErrorException with no description.
     */
    public ParseErrorException() {
        super();
    }
}
