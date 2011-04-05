package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * An integer literal.
 */
public class IntegerLiteral extends Literal {

    private Integer value;

    /**
     * Creates the integer literal from a given lexeme.
     */
    public IntegerLiteral(String lexeme) {
        super(lexeme);
    }

    /**
     * Returns the value.
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Analyzes this literal, computing its value.
     */
    public void analyze(Log log, SymbolTable table) {
        type = Type.INT;
        try {
            value = Integer.valueOf(getLexeme());
        } catch (NumberFormatException e) {
            log.error("bad.int", getLexeme());
        }
    }
}
