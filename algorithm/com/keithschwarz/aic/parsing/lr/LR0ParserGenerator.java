package com.keithschwarz.aic.parsing.lr;

/******************************************************************************
 * File: LR0ParserGenerator.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A parser generator capable of producting LR(0) parsers for grammars that are
 * LR(0).
 */




import java.util.*;

/**
 * A parser generator capable of producting LR(0) parsers for grammar that are
 * LR(0)
 * <p>
 * An LR(0) parser is the simplest type of canonical LR parser.  Canonical LR
 * parsers are directional, predictive, bottom-up parsers that attempt to
 * reduce the input string to the start symbol.  Whereas LL parsers work by
 * reconstructing a leftmost derivation, LR parsers work by tracing out the
 * rightmost derivation in reverse.
 * <p>
 * Internally, LR parsers are shift/reduce parsers.  They maintain a stack
 * called the work stack and perform a combination of shift actions and reduce
 * actions to reverse the input back to the start symbol.  In a shift action,
 * the parser takes the next token of input and places it atop the work stack.
 * In a reduce action, the parser pops off some number of symbols from the top
 * of the work stack and replace them with a nonterminal that could have
 * produced those symbols.  Just as a nondeterministic predict/match parser can
 * parse any grammar, a nondeterministic shift/reduce parser can parse any
 * grammar as well.  The LR(0) parser, however, is a deterministic parser that
 * can only handle a subset of all grammars, even unambiguous grammars.
 * <p>
 * To determine which action to take, the LR(0) parser uses a state machine
 * called an LR(0) automaton.  Each state in the LR(0) automaton corresponds to
 * a collection of LR(0) items that could potentially be under examination at
 * the moment.  Whenever a new symbol is read, the automaton shifts the symbol
 * and transitions into a new state, potentially applying a series of
 * reductions.
 * <p>
 * The states in the LR(0) automaton correspond to collections of LR(0) itmes.
 * The initial state corresponds to the item S &rarr; .w, where S is the start
 * symbol and S &rarr; w is the production associated with that start symbol
 * (the grammar is augmented so that there is a unique production for the start
 * symbol.  This initial LR(0) item is then augmented by adding in all possible
 * LR(0) items that might also be considered at the same time (this is called
 * the closure of the LR(0) item).  For example, if the LR(0) item S &rarr; .Ab
 * is an item in the state and A &rarr; c is a production, then the LR(0)
 * automaton would contain a state for the set { S &rarr; .Ab, A &rarr; .c }.
 * More generally, if A &rarr; x.Bw is an item in an LR(0) automaton state,
 * then for any production B &rarr; y, the item B &rarr; .y is also an item in
 * that state.
 * <p>
 * To determine whether to shift or reduce in a state, the LR(0) automaton
 * maintains two tables - an action table and a goto table.  The action table
 * tells the automaton what to do in a given state upon seeing some symbol -
 * either shift the symbol, apply a specific reduction, or end parsing.  The
 * goto table is the automaton's transition table as would be found in a
 * standard DFA.
 * <p>
 * To fill in the tables, we begin by constructing all possible LR(0) states.
 * Then, for each state, we look at the items it contains.  If the state
 * contains an LR(0) item of the form A &rarr; w.tx, where t is a terminal,
 * then on seeing symbol t we shift the symbol and transition into the state
 * containing the closure of A &rarr; wt.x.  If the state contains an LR(0)
 * item of the form A &rarr; w., then we reduce.  If there are two or more
 * actions in a state, we have an LR conflict - either a shift/reduce conflict
 * or a reduce/reduce conflict - and the grammar is not LR(0)
 * <p>
 * For more information on LR(0) parsers, consider reading "Compilers:
 * Principles, Tools, and Techniques, Second Edition" by Aho, Lam, Sathi, and
 * Ullman or "Parsing Techniques: A Practical Guide, Second Edition" by Grune
 * and Jacobs.
 */
public final class LR0ParserGenerator { 
    /* This class is not meant to be instantiated. */
    private LR0ParserGenerator() {
        // Empty //
    }

    /**
     * Given a grammar, constructs an LR(0) parser for that grammar.  If the
     * grammar is not LR(0), a GrammarNotLR0Exception is thrown.  The grammar
     * does not need to be augmented when passed into this function; it will
     * augment it internally.
     * <p>
     * Internally, this algorithm works in two passes.  First, the algorithm
     * constructs all of the LR(0) configurating sets for the grammar.  Second,
     * it revisits the grammar and constructs the action and goto tables for
     * the grammar.  For efficiency, the algorithm creates a "canonical" copy
     * of all of the LR(0) configurating sets which are then used to represent
     * the states of the automaton.
     *
     * @param g The grammar from which we should build the parser.
     * @return An LR(0) parser for that grammar.
     * @throws GrammarNotLR0Exception If the grammar is not LR(0).
     */
    public static Parser createParser(Grammar g) throws GrammarNotLR0Exception {
        /* Begin by augmenting the grammar so that we have a new, unique start
         * symbol.
         */
        g = GrammarTransforms.augmentGrammar(g);

        /* Construct the set of all LR(0) configurating sets.  These will serve
         * as the states in our automaton.
         */
        Set<Set<LRItem>> configuratingSets = constructConfiguratingSets(g);

        /* Create a mapping associating each configurating set with a
         * canonical configurating set.
         */
        Map<Set<LRItem>, Set<LRItem>> canonicalSets = 
            new HashMap<Set<LRItem>, Set<LRItem>>();
        for (Set<LRItem> item: configuratingSets)
            canonicalSets.put(item, item);

        /* Now, construct the action and goto tables for the different
         * configurating sets.
         */
        Map<TableKey, Set<LRItem>> gotoTable = 
            computeGotoTable(configuratingSets, canonicalSets, g);

        Map<Set<LRItem>, LRAction> actionTable =
            computeActionTable(configuratingSets, g);

        /* Find the initial state; this is the one containing the closure of
         * the initial production.
         */
        Set<LRItem> initial = canonicalSets.get(initialSetFor(g));

        return new LR0Parser(gotoTable, actionTable, initial);
    }

    /**
     * Given a grammar, returns the initial LR(0) configurating set for that
     * grammar.
     *
     * @param g The grammar from which we should get the configurating set.
     * @return The initial configurating set for that grammar.
     */
    private static Set<LRItem> initialSetFor(Grammar g) {
        /* Find the unique augmented production. */
        Production initialProd = g.getProductionsFor(g.getStartSymbol()).iterator().next();

        /* Construct the singleton set saying that we haven't seen anything in
         * it yet.
         */
        Set<LRItem> initialSet = new HashSet<LRItem>();
        initialSet.add(new LRItem(initialProd, 0, null));

        /* Hand back its closure. */
        return closureOf(initialSet, g);
    }

    /**
     * Given a grammar, constructs the LR(0) configurating sets for that
     * grammar.
     * <p>
     * The configurating sets for the grammar consist of all of the sets of
     * LR(0) items that could be active at any point during a parse.  It begins
     * with the closure of the item S' &rarr; .S, then transitions outward to
     * all other sets reachable from there.
     *
     * @param g The grammar whose configurating sets should be constructed.
     * @return The configurating sets for that grammar.
     */
    private static Set<Set<LRItem>> constructConfiguratingSets(Grammar g) {
        /* Allocate space for the result. */
        Set<Set<LRItem>> configuratingSets = new HashSet<Set<LRItem>>();

        /* Maintain a worklist of configurating sets that need to be 
         * processed.
         */
        Queue<Set<LRItem>> worklist = new LinkedList<Set<LRItem>>();

        /* Seed the worklist with closure of the initial production. */
        worklist.offer(initialSetFor(g));

        /* Continuously dequeue and process these sets until all configurating
         * sets have been generated.
         */
        while (!worklist.isEmpty()) {
            Set<LRItem> curr = worklist.remove();

            /* If we've seen this set before, skip it. */
            if (!configuratingSets.add(curr)) continue;

            /* Otherwise, get the set of all outgoing transitions from this
             * set and add all of them into the worklist.
             */
            Map<Symbol, Set<LRItem>> outgoing = findSuccessors(curr, g);
            for (Set<LRItem> succ: outgoing.values())
                worklist.offer(succ);
        }
                       
        return configuratingSets;                       
    }

    /**
     * Given a partial set of LR(0) items, returns the closure of that set.
     * <p>
     * The closure of a set of LR(0) items is defined as the smallest set S
     * with the following properties:
     * <ul>
     * <li> For any item I in the initial set, I is in S. </li>
     * <li> If A &rarr; w.Bx in S, where B is a nonterminal, and for any
     *      production B &rarr; y, B &rarr; .y is in S.</li>
     * </ul>
     *
     * @param initial The initial set of LR(0) items.
     * @param g The grammar in which the LR(0) item resides.
     * @return The closure of the initial set.
     */
    private static Set<LRItem> closureOf(Set<LRItem> initial,
                                         Grammar g) {
        /* Allocate space for the result. */
        Set<LRItem> result = new HashSet<LRItem>();

        /* Add everything from the initial set into a worklist. */
        Queue<LRItem> worklist = new LinkedList<LRItem>(initial);

        /* While there are items left to process, continuously process the
         * items.
         */
        while (!worklist.isEmpty()) {
            LRItem curr = worklist.remove();

            /* If we've already seen this LR item, skip it. */
            if (!result.add(curr)) continue;

            /* If the symbol after the dot is a nonterminal, then we need to
             * add in new LR(0) items corresponding to all of the possible
             * productions we could be starting.
             */
            Symbol next = curr.getSymbolAfterDot();
            if (next instanceof Nonterminal) {
                for (Production p: g.getProductionsFor((Nonterminal) next))
                    worklist.add(new LRItem(p, 0, null));
            }
        }
        return result;
    }

    /**
     * Given an LR(0) configurating set, returns a map containing the
     * successors of that set.
     * <p>
     * For any LR(0) item A &rarr; w.tx, where t is an arbitrary symbol, the
     * successor of that item for the symbol t is A &rarr; wt.x.  If we take
     * the symbol t and compute the set of all LR(0) items that are the
     * successors of some LR(0) item in the configurating set, we end up with
     * some set of LR(0) items that could be active if we were in the original
     * set of LR(0) items and saw some symbol.  If we then take the closure of
     * that set, we get a new LR(0) configurating set corresponding to the set
     * of all LR(0) items that we might be processing if we had seen some
     * particular symbol while in some configurating set.
     * <p>
     * This function accepts an LR(0) configurating set and returns a map that
     * associates each symbol for which some successor is defined with the new
     * LR(0) configurating set associated with that symbol.
     *
     * @param set An LR(0) configurating set.
     * @param g The grammar for which this set is defined.
     * @return A map associating grammar symbols with the successors of the
     *         configurating set 'set'.
     */
    private static Map<Symbol, Set<LRItem>>
        findSuccessors(Set<LRItem> set, Grammar g) {
        Map<Symbol, Set<LRItem>> result = new HashMap<Symbol, Set<LRItem>>();

        /* Begin by placing the successors of each LR item into the result set
         * without taking the closure.
         */
        for (LRItem item: set) {
            /* See what's after the dot.  If there's nothing there, then we
             * can skip this item.
             */
            Symbol afterDot = item.getSymbolAfterDot();
            if (afterDot == null) continue;

            /* Make sure we've a set to place the new item into! */
            if (!result.containsKey(afterDot))
                result.put(afterDot, new HashSet<LRItem>());

            /* Place the successor for this item into that set. */
            result.get(afterDot).add(item.withDotMovedForward());
        }

        /* Compute the closures of each successor set. */
        for (Map.Entry<Symbol, Set<LRItem>> entry: result.entrySet())
            entry.setValue(closureOf(entry.getValue(), g));

        return result;
    }
    /**
     * Computes the GOTO table for the LR(0) parser.
     * <p>
     * The GOTO table for an LR(0) parser is a standard transition table for a
     * DFA whose states are the configurating sets and with transitions between
     * configurating sets and their successors.
     *
     * @param configurations The set of all LR(0) configurating sets.
     * @param canonicalMap A map used to associate configurating sets with
     *        their canonical representations.
     * @param g The grammar to be parsed.
     * @return The GOTO table for the LR(0) parser.
     */
    private static Map<TableKey, Set<LRItem>>
        computeGotoTable(Set<Set<LRItem>> configurations,
                         Map<Set<LRItem>, Set<LRItem>> canonicalMap,
                         Grammar g) {
        /* Allocate space for the result. */
        Map<TableKey, Set<LRItem>> gotoTable = 
            new HashMap<TableKey, Set<LRItem>>();

        /* Visit each configuration and fill in the GOTO table. */
        for (Set<LRItem> state: configurations) {
            /* Find all the successors of this state.  The remapped versions
             * will be the successors of this state in the GOTO table.
             */
            for (Map.Entry<Symbol, Set<LRItem>> succ: findSuccessors(state, g).entrySet()) {
                /* This state's successor state on the given symbol is the
                 * canonical state equal to the successor.
                 */
                gotoTable.put(new TableKey(state, succ.getKey()),
                              canonicalMap.get(succ.getValue()));
                
            }
        }
        return gotoTable;
    }
    
    /**
     * Computes the ACTION table for the LR(0) parser.
     * <p>
     * The ACTION table is a table that contains either shift or reduce actions
     * based on what elements are found in the configurating set.  A set that
     * does not contain any reduce actions has a shift action.  A set with just
     * one reduce action is a reduce step (and performs precisely that
     * reduction).  A set with more than one option leads to a shift/shift or
     * reduce/reduce conflict.
     *
     * @param configurations The canonical LR(0) configurating sets.
     * @param g The grammar this is defined relative to.
     * @return The ACTION table for the grammar.
     * @throws GrammarNotLR0Exception If the grammar is found to not be LR(0).
     */
    private static Map<Set<LRItem>, LRAction>
        computeActionTable(Set<Set<LRItem>> configurations, Grammar g) 
    throws GrammarNotLR0Exception {
        /* The resulting action table will be an identity map because we are
         * using the canonical representations of the sets of LR(0) items.
         */
        Map<Set<LRItem>, LRAction> actionTable =
            new IdentityHashMap<Set<LRItem>, LRAction>();

        /* Visit all LR(0) configurating sets, populating the action table with
         * the appropriate information.
         */
        for (Set<LRItem> items: configurations) {
            /* Track what action we should take, which initially is null. */
            LRAction action = null;

            /* For each item in the set, see what production to apply. */
            for (LRItem item: items) {
                /* If this item is complete, we should reduce. */
                if (item.isComplete()) {
                    /* The action we take will either be a reduce action (if
                     * the production doesn't involve the start symbol) or an
                     * accept action (if it does).
                     */

                    LRAction reduce = (item.getProduction().getNonterminal().equals(g.getStartSymbol())? 
                                       AcceptAction.INSTANCE : 
                                       new ReduceAction(item.getProduction()));

                    /* If we are supposed to be doing something else here,
                     * report an error.
                     */
                    if (action != null)
                        throw new GrammarNotLR0Exception("Conflict: " + action + " vs " + reduce);

                    /* Otherwise, set that to be our action. */
                    action = reduce;
                }
                /* Otherwise, this should be a shift action. */
                else {
                    if (action != null && action != ShiftAction.INSTANCE)
                        throw new GrammarNotLR0Exception("Conflict: " + action + " vs " + ShiftAction.INSTANCE);
                                 
                    action = ShiftAction.INSTANCE;
                }
            }
            
            /* Set the action for this state to be the action we've just
             * found.
             */
            actionTable.put(items, action);
        }
        return actionTable;
    }

    /**
     * A utility type representing a pair of a state and a symbol.  This is
     * used internally by the parser to represent keys in the goto table.
     * <p>
     * It is assumed that the states are represented by the canonical versions
     * of the configurating sets constructed by the parser generator.
     */
    private static final class TableKey {
        /** The automaton state. */
        private final Set<LRItem> state;

        /** The symbol that this action is keyed on. */
        private final Symbol symbol;

        /**
         * Constructs a new TableKey from the given state/symbol pair.
         *
         * @param state The state of the key.
         * @param symbol The symbol of the key.
         */
        public TableKey(Set<LRItem> state, Symbol symbol) {
            assert state != null;
            assert symbol != null;

            this.state = state;
            this.symbol = symbol;
        }

        /**
         * Checks whether this TableKey is equal to some other object.
         *
         * @param o The object to compare to.
         * @return Whether this object is equal to it.
         */
        @Override 
        public boolean equals(Object o) {
            /* Confirm the other object is a TableKey; fail if not. */
            if (!(o instanceof TableKey)) return false;

            /* Downcast and check for equality.  Note that in the equality
             * check we use reference equality rather than deep equality to
             * check whether the two states are the same.
             */
            TableKey other = (TableKey)o;
            return state == other.state && symbol.equals(other.symbol);
        }

        /**
         * Returns a hash code for this object.
         *
         * @return A hash code for this object.
         */
        @Override 
        public int hashCode() {
            /* Because we compare states by reference equality, we use the
             * identity hash code.
             */
            return 31 * System.identityHashCode(state) + symbol.hashCode();
        }

        /**
         * Returns a human-readable representation of this key.
         *
         * @return A human-readable representation of this key.
         */
        @Override 
        public String toString() {
            return "(" + state + ", " + symbol + ")";
        }
    }

    /**
     * An LR(0) parser.
     * <p>
     * Internally, the LR(0) parser maintains an action table, a goto table,
     * and a parsing stack.  The parsing stack holds parse trees for the
     * symbols on the stack, along with an indication of what state the parse
     * stack is in.  That way, when we apply a reduction, we (1) have quick
     * access to the parse trees that need to be joined together, and (2) can
     * quickly reestablish which state the parser is in.
     */
    private static final class LR0Parser implements Parser {
        /** The goto table. */
        private final Map<TableKey, Set<LRItem>> gotoTable;

        /** The action table. */
        private final Map<Set<LRItem>, LRAction> actionTable;

        /**
         * A utility type representing a pair of a parse tree and a state.
         * These objects comprise the parsing stack.
         */
        private static final class StackEntry {
            /** 
             * The parse tree for this node, or null if this is the bottom of
             * the stack.
             */
            public final ParseTree tree;

            /**
             * The state the parser was in when this symbol was pushed atop
             * the stack.
             */
            public final Set<LRItem> state;

            /**
             * Constructs a new StackEntry for the given parse tree and
             * automaton state.
             *
             * @param tree The parse tree for this entry.
             * @param state The automaton state for this entry.
             */
            public StackEntry(ParseTree tree, Set<LRItem> state) {
                this.tree = tree;
                this.state = state;
            }
        }

        /** The parsing stack. */
        private final Deque<StackEntry> parsingStack = new ArrayDeque<StackEntry>();

        /** If we've hit an ACCEPT action, the generated parse tree. */
        private ParseTree result = null;

        /**
         * Constructs a new LR(0) parser that uses the specified ACTION and
         * GOTO tables.
         *
         * @param gotoTable The GOTO table.
         * @param actionTable The ACTION table.
         * @param initialState The initial machine state.
         */
        public LR0Parser(Map<TableKey, Set<LRItem>> gotoTable,
                         Map<Set<LRItem>, LRAction> actionTable,
                         Set<LRItem> initialState) {
            this.gotoTable = gotoTable;
            this.actionTable = actionTable;

            /* Set up the parsing stack by pushing an entry onto it that just
             * holds the initial machine state.
             */
            parsingStack.offerLast(new StackEntry(null, initialState));
        }

        /**
         * Processes a new token of input.
         * <p>
         * When we see a new token, we take the appropriate shift action (if
         * one happens to exist), then continuously apply reduce and accept
         * actions until there aren't any more to apply.  This allows us to
         * maintain the invariant that the LR(0) automaton's state will always
         * be a shift state upon function return, unless the automaton has
         * already accepted.
         *
         * @param terminal The next terminal to process.
         * @throws ParseErrorException If there is no shift action defined for
         *         that terminal.
         */
        public void nextTerminal(Terminal terminal) throws ParseErrorException {
            /* If we have already accepted (that is, the resulting parse tree
             * is non-null), then we should report an error rather than gumming
             * up the internal parsing logic.
             */
            if (result != null)
                throw new ParseErrorException("Expected EOF, found " + terminal);

            /* See what state we're in. */
            Set<LRItem> state = parsingStack.peekLast().state;

            /* The action defined here must be a shift (otherwise we would have
             * already reduced or accepted).  See where it goes.
             */
            assert actionTable.get(state) instanceof ShiftAction;
            Set<LRItem> newState = gotoTable.get(new TableKey(state, terminal));

            /* We might not have a state to go to if there is no defined
             * transition.  If that happens, report an error.
             */
            if (newState == null)
                throw new ParseErrorException("Unexpected " + terminal);

            /* Otherwise, shift the token onto the stack and enter this new
             * state.
             */
            parsingStack.offerLast(new StackEntry(new ParseTree(terminal), newState));

            /* Now, while the current action to apply is a reduce or an accept,
             * keep applying that action.
             */
            while (true) {
                LRAction action = actionTable.get(newState);

                /* If the action is a shift action, we're done. */
                if (action instanceof ShiftAction) return;

                /* If the action is an accept action, copy the parse tree out
                 * of the stack element and stop.
                 */
                else if (action instanceof AcceptAction) {
                    this.result = parsingStack.peekLast().tree;
                    return;
                }

                /* Otherwise, we have a reduce action.  Apply the appropriate
                 * reduction and see where the transition takes us.
                 */
                else {
                    assert action instanceof ReduceAction;
                    Production toReverse = ((ReduceAction) action).getProduction();

                    /* We need to reduce some number of terminals and
                     * nonterminals back up to a nonterminal that derived them.
                     * This requires us to update the parsing stack, create
                     * a new parse tree node for the new nonterminal, and to
                     * shift the new nonterminal that was just added (it has
                     * to be a shift action; we don't need to double-check
                     * this).
                     *
                     * To begin, we'll pop off the appropriate number of
                     * symbols from the stack.  Since they're in the reverse
                     * order from what they should be in the production, we'll
                     * add them to a LinkedList in reverse order.
                     */
                    LinkedList<ParseTree> children = new LinkedList<ParseTree>();
                    for (int i = 0; i < toReverse.getProduction().size(); ++i) {
                        children.offerFirst(parsingStack.removeLast().tree);
                    }

                    /* Construct a new parse tree node for the appropriate
                     * nonterminal that has these children.
                     */
                    ParseTree newTree = new ParseTree(toReverse.getNonterminal(),
                                                      children);

                    /* The parsing stack should not be empty, but should tell
                     * us what state was just exposed.  We will use this, in
                     * conjunction with the nonterminal we just pushed, to
                     * determine where we go to next.
                     */
                    assert !parsingStack.isEmpty();
                    newState = parsingStack.peekLast().state;

                    /* Consult the GOTO table to see what state we now end up
                     * in.  Notice that we have just updated newState to be
                     * this new state, but have not yet pushed it atop the
                     * parsing stack.
                     */
                    newState = gotoTable.get(new TableKey(newState,
                                                          toReverse.getNonterminal()));
                    assert newState != null;

                    /* Push this new state, along with the new parse tree, atop
                     * the parsing stack.
                     */
                    parsingStack.offerLast(new StackEntry(newTree, newState));

                    /* We now loop again to see if we need to do another
                     * reduction.
                     */
                }
            }            
        }

        /**
         * Indicates that the end of the input has been reached, returning the
         * generated parse tree if one is available.
         * <p>
         * Since LR(0) parsers have absolutely no lookahead, this method can
         * simply check whether an accept action has been taken and can hand
         * back the generated parse tree if so.  If not, it reports an error.
         *
         * @return The computed parse tree.
         * @throws ParseErrorException If there is no parse tree available.
         */
        public ParseTree inputComplete() throws ParseErrorException {
            if (result != null)
                return result;

            throw new ParseErrorException("Unexpected EOF");
        }
    }
}
