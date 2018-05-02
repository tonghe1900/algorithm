package com.keithschwarz.aic.parsing.lr;
/******************************************************************************
 * File: ParserUtils.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * Utility functions for manipulating parsers.
 */

import java.util.*;

/**
 * A class containing utility functions for working with parsers.
 *
 * @author Keith Schwarz (htiek@cs.stanford.edu)
 */
public final class ParserUtils {
    /* This class is not meant to be instantiated. */
    private ParserUtils() {

    }

    /**
     * Runs a parser on a given stream of terminals.
     * <p>
     * This function either hands back the completed parse tree or raises an
     * exception if a parse error occurs.
     * 
     * @param parser The parser to run.
     * @param terminals The input to parse.
     * @return The completed parse tree.
     * @throws ParseErrorException If a parse error occurs.
     */
    public static ParseTree 
        parseSequence(Parser parser,
                      List<Terminal> terminals) throws ParseErrorException {
        for (Terminal t: terminals)
            parser.nextTerminal(t);
        return parser.inputComplete();
    }
}
