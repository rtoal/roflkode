package edu.lmu.cs.xlg.roflkode.entities;

/**
 * Abstract superclass for all literals.
 */
public abstract class Literal extends Expression {

    private String lexeme;

    /**
     * Creates a literal, given its lexeme.
     */
    public Literal(String lexeme) {
        this.lexeme = lexeme;
    }

    /**
     * Returns the lexeme.
     */
    public String getLexeme() {
        return lexeme;
    }
}
