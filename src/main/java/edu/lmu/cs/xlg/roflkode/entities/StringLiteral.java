package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

public class StringLiteral extends Literal {

    private List<Integer> values;

    /**
     * Creates a string literal from a lexeme that includes the double quote delimiters.
     */
    public StringLiteral(String lexeme) {
        super(lexeme);
    }

    /**
     * Returns the list of codepoints for the characters in the string.
     */
    public List<Integer> getValues() {
        return values;
    }
}
