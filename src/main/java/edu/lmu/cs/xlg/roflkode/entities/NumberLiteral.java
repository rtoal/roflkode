package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A number literal, like "2.3", "4.6634E-231", etc.
 */
public class NumberLiteral extends Literal {

    /**
     * Creates a real literal from its lexeme.
     */
    public NumberLiteral(String lexeme) {
        super(lexeme);
    }
}
