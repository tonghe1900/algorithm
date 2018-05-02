package com.keithschwarz.aic.parsing.lr;
/******************************************************************************
 * File: Parser.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An interface representing an object that can parse a sequence of terminals
 * into a parse tree.  The parser object can operate one token at a time, or
 * it can be run on an entire prebuilt sequence of terminals.  To keep the
 * implementation simple, parsers are only required to work once and can fail
 * if run multiple times.
 */

import java.util.*;

/**
 * An interface representing an object that can parse a sequence of terminals
 * into a parse tree.
 * <p>
 * The parser is designed to operate one token at a time.  To introduce a new
 * token to the parser, you may use the Parser#nextTerminal method.  Once all
 * input has been processed, you should call the Parser#inputComplete method
 * to obtain a complete parse tree for the input.  If at any point the parser 
 * encounters a parse error, a ParseErrorException exception is thrown.
 * <p>
 * For implementation simplicity, the Parser interface only requires the
 * parser to support a single pass over the input.  Calling #nextTerminal after
 * the input has been completely processed may result in ParseErrorExceptions.
 *
 * @author Keith Schwarz (htiek@cs.stanford.edu)
 */
public interface Parser {
    /**
     * Feeds another terminal into the parser, potentially causing the parser
     * to continue parsing or issue a parse error.
     *
     * @param terminal The terminal to feed into the parser.
     * @throws ParseErrorException If a parse error occurs.
     */
    public void nextTerminal(Terminal terminal) throws ParseErrorException;

    /**
     * Indicates to the parser that the end of input has been reached, causing
     * the parser to hand back the parse tree it has created so far.  If a
     * parse error occurs when doing so, the parser may issue a parse error.
     *
     * @return The completed parse tree.
     * @throws ParseErrorException If a parse error occurs.
     */
    public ParseTree inputComplete() throws ParseErrorException;
}
