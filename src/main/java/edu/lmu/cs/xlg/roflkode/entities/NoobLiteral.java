package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A singleton class for an object representing the literal N00B.
 */
public class NoobLiteral extends Literal {

    public static NoobLiteral INSTANCE = new NoobLiteral();

    // Constructor is private because this class is a singleton.
    private NoobLiteral() {
        super("N00B");
    }

    /**
     * Analyzes this literal.
     */
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        type = Type.N00B_TYPE;
    }
}
