package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A literal of type string.  A string literal is represented as a list of codepoints.
 */
public class StringLiteral extends Literal {

    private List<Integer> codepoints;

    /**
     * Creates a string literal from a lexeme that includes the double quote delimiters.
     */
    public StringLiteral(String lexeme) {
        super(lexeme);
    }

    /**
     * Returns the list of codepoints for the characters in the string.
     */
    public List<Integer> getCodepoints() {
        return codepoints;
    }


    /**
     * Analyzes this literal, gathering up codepoints.
     */
    public void analyze(Log log, SymbolTable table) {
        type = Type.YARN;
        codepoints = KarLiteral.codepoints(getLexeme(), 1, getLexeme().length() - 1, log);
    }
}
