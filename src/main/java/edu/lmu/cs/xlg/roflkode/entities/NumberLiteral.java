package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A number literal, like "2.3", "4.6634E-231", etc.
 */
public class NumberLiteral extends Literal {

    private Double value;

    /**
     * Creates a real literal from its lexeme.
     */
    public NumberLiteral(String lexeme) {
        super(lexeme);
    }

    /**
     * Returns the value.
     */
    public Double getValue() {
        return value;
    }

    /**
     * Analyzes this literal, determining its value.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        type = Type.NUMBR;
        try {
            value = Double.valueOf(getLexeme());
        } catch (NumberFormatException e) {
            log.error("bad_number", getLexeme());
        }
    }
}
