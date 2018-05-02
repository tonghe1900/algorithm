

/******************************************************************************
 * File: LL1ParserGenerator.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A parser generator that produces LL(1) parsers for a grammar.
 */

package com.keithschwarz.aic.parsing.analysis;
import com.keithschwarz.aic.parsing.analysis.*;
import com.keithschwarz.aic.parsing.*;
import java.util.*;

/**
 * A parser generator that generates LL(1) parsers for a given grammar.  An
 * LL(1) parser is a simple but powerful top-down, predictive, directional
 * parser that works by tracing out a leftmost derivation during a left-to-
 * right scan of the input.
 * <p>
 * LL(1) parsers are predict/match parsers, meaning that they are allowed to
 * use two different types of moves: predict moves, which expand out a
 * nonterminal to a particular production; and match moves, which match the
 * current terminal symbol of the input with the predicted terminal symbol.
 * <p>
 * Internally, LL(1) parsers maintain a stack called the prediction stack.
 * This stack initially is seeded with S$, where S is the start symbol of the
 * grammar and $ is the special "end-of-input" marker.  Upon seeing a new
 * token of input, if the top of the stack is a nonterminal, the LL(1) parser
 * predicts which production it should apply in order to correctly match that
 * input.  If the top of the stack is a terminal, the LL(1) parser tries to
 * match that terminal symbol with the next terminal symbol.  If at some point
 * an error occurs (either there is no production to predict, or some token is
 * found that does not match the given terminal), the parser fails.
 * <p>
 * Driving the LL(1) parser is an LL(1) parsing table that for each pair of a
 * terminal and nonterminal symbol says which production to apply if the given
 * nonterminal symbol is atop the parsing stack and the given terminal is the
 * next terminal in the input sequence.  This table is constructed from the
 * FIRST and FOLLOW sets of the grammar.  Not all grammars can have such a
 * table constructed, however.  For example, this grammar is not LL(1):
 * <p>
 *    S -&gt; a | ab
 * <p>
 * Since if the current nonterminal is S and the current input symbol is a, it
 * is not clear which production ought to be used.  If the parser generator
 * tries to construct an LL(1) parser for a grammar that is not LL(1), a
 * GrammarNotLL1Exception will be raised.
 * <p>
 * For more information on how LL(1) parsers work, consider reading the most
 * excellent textbook "Parsing Techniques: A Practical Guide, 2nd Edition" by
 * Grune and Jacobs, which has perhaps the best treatment of the subject.
 *
 * @author Keith Schwarz (htiek@cs.stanford.edu)
 */
public final class LL1ParserGenerator {
    /* This class is not meant to be instantiated. */
    private LL1ParserGenerator() {
        // Empty //
    }

    /**
     * Given a grammar, constructs an LL(1) parser for that grammar.  If the
     * parser can be formed, it is returned.  If the grammar is not LL(1), an
     * exception is raised.
     *
     * @param g The grammar to build an LL(1) parser for.
     * @return An LL(1) parser for that grammar.
     * @throws GrammarNotLL1Exception If the grammar is not LL(1).
     */
    public static Parser createParser(Grammar g) throws GrammarNotLL1Exception {
        /* Sanity-check the input. */
        if (g == null) throw new NullPointerException();

        /* Compute the FIRST and FOLLOW sets for the grammar. */
        Map<Nonterminal, Set<GeneralizedSymbol>> first =
            GrammarAttributes.computeFirstSets(g);
        Map<Nonterminal, Set<GeneralizedSymbol>> follow =
            GrammarAttributes.computeFollowSets(g, first);

        /* Allocate space for the parsing table. */
        Map<LL1Key, List<Symbol>> parsingTable = 
            new HashMap<LL1Key, List<Symbol>>();

        /* Begin filling in the parsing table.  The rules for filling in this
         * table are relatively straightforward:
         *
         * For each production A -> w, the production to use on seeing the pair
         * (A, t) for any terminal t in FIRST(A) is w.  That is, if we want to
         * match a t, we should do so by expanding out A -> w, then matching
         * the t with the first character of w.
         *
         * If FIRST(A) contains epsilon, then for each t in FOLLOW(A), the
         * production to use on seeing (A, t) is w.  That is, if A is nullable
         * and we see a symbol that should come after A, the best option is to
         * expand A to w and then expand everything in w to the empty string.
         *
         * If any conflicts arise in this process, we report an error because
         * the grammar is not LL(1).
         */
        for (Production prod: g) {
            /* Get the FIRST set for the production. */
            Set<GeneralizedSymbol> firstSymbols =
                GrammarAttributes.getFirstSetForSequence(prod.getProduction(),
                                                         first);

            /* Add every non-epsilon production into the parsing table. */
            for (GeneralizedSymbol s: firstSymbols) {
                if (s.equals(SpecialSymbols.EPSILON)) continue;

                /* Add this entry to the parsing table, failing if something
                 * was already there.
                 */
                if (parsingTable.put(new LL1Key(prod.getNonterminal(), s),
                                     prod.getProduction()) != null)
                    throw new GrammarNotLL1Exception("Conflict detected for " + prod.getNonterminal() + ", " + s);
            }

            /* Now, if the FIRST set contains epsilon, add in similar rules for
             * everything in the FOLLOW set.
             */
            if (firstSymbols.contains(SpecialSymbols.EPSILON)) {
                for (GeneralizedSymbol s: follow.get(prod.getNonterminal())) {
                    /* Add this entry to the parsing table, failing if 
                     * something was already there.
                     */
                    if (parsingTable.put(new LL1Key(prod.getNonterminal(), s),
                                         prod.getProduction()) != null)
                        throw new GrammarNotLL1Exception("Conflict detected for " + prod.getNonterminal() + ", " + s);
                }
            }
        }

        /* Wrap the parsing table up into a parser, then hand it back. */
        return new LL1Parser(parsingTable, g.getStartSymbol());
    }

    /**
     * A class representing a pair of a nonterminal and a terminal that acts as
     * the key in an LL(1) parsing table.
     */
    private static final class LL1Key {
        /** The nonterminal used in the key. */
        private final Nonterminal nonterminal;

        /** The terminal (or EOF) used in the key. */
        private final GeneralizedSymbol terminal;

        /**
         * Constructs a new LL1Key holding the given nonterminal/terminal pair.
         *
         * @param nonterminal The nonterminal in the key.
         * @param terminal The terminal in the key.
         */
        public LL1Key(Nonterminal nonterminal, GeneralizedSymbol terminal) {
            /* Check the input for correctness. */
            if (nonterminal == null || terminal == null)
                throw new NullPointerException();

            this.nonterminal = nonterminal;
            this.terminal = terminal;
        }

        /**
         * Returns whether this LL1Key is equal to some other object.
         *
         * @param o The object to compare again.
         * @return Whether this object is equal to o.
         */
        @Override 
        public boolean equals(Object o) {
            /* The other object must be an LL1Key. */
            if (!(o instanceof LL1Key)) return false;

            /* Downcast to the actual LL1Key. */
            LL1Key other = (LL1Key) o;

            /* See if we have the same terminal/nonterminal pair. */
            return terminal.equals(other.terminal) &&
                   nonterminal.equals(other.nonterminal);
        }

        /**
         * Returns a hash code for this LL1Key.
         *
         * @return A hash code for this LL1Key.
         */
        @Override 
        public int hashCode() {
            return 31 * nonterminal.hashCode() + terminal.hashCode();
        }

        /**
         * Returns a human-readable description of this LL1Key.
         *
         * @return A human-readable description of this LL1Key.
         */
        @Override 
        public String toString() {
            return "(" + nonterminal + ", " + terminal + ")";
        }
    }

    /** 
     * An LL(1) parser.  Internally.  The parser maintains a stack containing
     * the predicted symbols, along with an LL(1) parsing table telling it
     * which actions to take.
     */
    private static final class LL1Parser implements Parser {
        /**
         * A utility struct pairing a symbol and the parse tree it corresponds
         * to.  The LL(1) parser will maintain a sequence of these elements so
         * that we can build up a parse tree for the input as we go.
         */
        private static final class StackEntry {
            /** The symbol (or EOF) in the stack. */
            public final GeneralizedSymbol symbol;

            /** The parse tree associated with that symbol. */
            public final ParseTree tree;

            /**
             * Constructs a new StackEntry holding the given symbol and parse
             * tree.
             *
             * @param symbol The symbol to store here.
             * @param tree The parse tree to store here.
             */
            public StackEntry(GeneralizedSymbol symbol, ParseTree tree) {
                this.symbol = symbol;
                this.tree = tree;
            }
        }

        /** The parsing table. */
        private final Map<LL1Key, List<Symbol>> parsingTable;

        /** The parsing stack. */
        private final Deque<StackEntry> parsingStack = new ArrayDeque<StackEntry>();

        /** The generated parse tree. */
        private final ParseTree parseTree;

        /**
         * Constructs a new LL(1) parser using the given parse table and start
         * symbol.  The parsing stack is seeded with the start symbol.
         *
         * @param parsingTable The parsing table.
         * @param start The start symbol.
         */
        public LL1Parser(Map<LL1Key, List<Symbol>> parsingTable,
                         Nonterminal start) {
            this.parsingTable = parsingTable;

            /* Create a new parse tree seeded with the start symbol. */
            parseTree = new ParseTree(start);

            /* Put the EOF marker atop the stack. */
            parsingStack.offerFirst(new StackEntry(SpecialSymbols.EOF, null));

            /* Insert a pair of the start symbol/parse tree atop the parsing
             * stack.
             */
            parsingStack.offerFirst(new StackEntry(new ConcreteSymbol(start), parseTree));
        }

        /**
         * Consumes the next terminal symbol, applying a predict/match step
         * as necessary.  This may cause multiple predict steps to be applied
         * before a match step is made.  If no prediction exists or if the
         * match step fails, a ParseErrorException is raised.
         *
         * @param terminal The next terminal symbol.
         * @throws ParseErrorException If a parse error occurs.

         */
        @Override 
        public void nextTerminal(Terminal terminal) throws ParseErrorException {
            processSymbol(new ConcreteSymbol(terminal));
        }

        /**
         * Processes the end of the input.  This should conclude with a series
         * of predicts and matches that ultimately empties the stack and
         * returns the parse tree.
         *
         * @return The parse tree generated by the parser.
         * @throws ParseErrorException If EOF wasn't expected.
         */
        @Override
        public ParseTree inputComplete() throws ParseErrorException {
            ParseTree result = processSymbol(SpecialSymbols.EOF);
            assert result != null;
            return result;
        }

        /**
         * Private helper function that processes the next token of the input,
         * which can be either a terminal or the EOF marker.  If a parsing
         * error occurs, a ParseErrorException is thrown.  If parsing completes
         * because the input was the EOF marker, the parse tree is returned.
         *
         * @param symbol The generalized symbol that appears next.
         * @return The completed parse tree, if any.
         * @throws ParseErrorException If a parse error occurs.
         */
        private ParseTree processSymbol(GeneralizedSymbol symbol) throws ParseErrorException {
            /* If the stack is empty, then we're done parsing and can't process
             * any more terminals.
             */
            if (parsingStack.isEmpty())
                throw new ParseErrorException("Parsing already completed.");

            /* Keep applying predict steps until the top of the stack holds a
             * terminal symbol.
             */
            while (true) {
                /* Look at the top of the stack to see what we find. */
                StackEntry top = parsingStack.pollFirst();

                /* If the top symbol matches the symbol we just encountered,
                 * the match is complete.
                 */
                if (top.symbol.equals(symbol)) {
                    /* If the input was EOF, hand back the parse tree.
                     * Otherwise, hand back null as a sentinel.
                     */
                    return symbol.equals(SpecialSymbols.EOF) ? parseTree : null;
                }

                /* If the top symbol didn't match, then one of two things must
                 * be true.  First, we could have a match failure, where the
                 * symbol in question does not match the symbol atop the stack.
                 * Since we know the top of the stack doesn't match the current
                 * symbol, the only way that we didn't have a mismatch is if
                 * the top of the stack is not a nonterminal.
                 */
                if (!(top.symbol instanceof ConcreteSymbol &&
                      ((ConcreteSymbol)top.symbol).getSymbol() instanceof Nonterminal))
                    throw new ParseErrorException("Expected " + top.symbol + ", found " + symbol);

                /* Otherwise, the top of the stack must be a nonterminal and
                 * we need to do a predict step.
                 */
                Nonterminal nonterminal = (Nonterminal)((ConcreteSymbol)top.symbol).getSymbol();
                List<Symbol> production = parsingTable.get(new LL1Key(nonterminal, symbol));

                /* If no production is defined, then we have a parse error. */
                if (production == null)
                    throw new ParseErrorException("No production for " + nonterminal + " on seeing " + symbol);

                /* Otherwise, push each symbol onto the stack, annotated with
                 * a new parse tree node.  However, because the stack grows
                 * in the front, we have to push these symbols in the reverse
                 * order from when they appear.
                 */
                for (int i = production.size() - 1; i >= 0; --i) {
                    Symbol s = production.get(i);

                    /* Construct a new parse tree node for this symbol. */
                    ParseTree tree = new ParseTree(s);

                    /* Insert this symbol/tree pair at the top of the stack. */
                    parsingStack.offerFirst(new StackEntry(new ConcreteSymbol(s), tree));
                }

                /* Now, looking over the first |w| symbols of the stack, add
                 * each as a child of the parse tree node for the nonterminal
                 * we just expanded.
                 */
                Iterator<StackEntry> iter = parsingStack.iterator();
                for (int i = 0; i < production.size(); ++i)
                    top.tree.getChildren().add(iter.next().tree);
            }
        }
    }
}
