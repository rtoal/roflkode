package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A literal of type KAR.
 */
public class KarLiteral extends Literal {

    /**
     * Creates a character literal given its lexeme. The lexeme does contain the single quote
     * delimiters.
     */
    public KarLiteral(String lexeme) {
        super(lexeme);
    }
}
