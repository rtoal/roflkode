package edu.lmu.cs.xlg.roflkode.entities;

import java.util.ArrayList;
import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A literal of type KAR.
 */
public class KarLiteral extends Literal {

    private Integer codepoint;

    /**
     * Creates a character literal given its lexeme. The lexeme does contain the single quote
     * delimiters.
     */
    public KarLiteral(String lexeme) {
        super(lexeme);
    }

    public Integer getCodepoint() {
        return codepoint;
    }

    /**
     * Perform semantic analysis on this literal, figuring out the codepoint from the lexeme.
     */
    public void analyze(Log log, SymbolTable table) {
        type = Type.KAR;

        // When figuring out codepoint, don't consider the single quotes.
        List<Integer> values = codepoints(getLexeme(), 1, getLexeme().length() - 1, log);
        this.codepoint = values.get(0);
    }

    /**
     * Returns a list of the codepoints of the characters in the given string from position start
     * (inclusive) to position end (exclusive).  Precondition: The string s is lexically valid --
     * in practice this method is only called during semantic analysis, after the script has
     * already been scanned and parsed, and we know the string will be fine in that case,
     */
    public static List<Integer> codepoints(String s, int start, int end, Log log) {
        List<Integer> result = new ArrayList<Integer>(end - start);
        for (int pos = start; pos < end; pos++) {
            char c = s.charAt(pos);
            if (c == ':') {
                c = s.charAt(++pos);
                if (c == ')') result.add(0x0A);
                else if (c == '>') result.add(0x09);
                else if (c == '\"') result.add(0x22);
                else if (c == '\'') result.add(0x27);
                else if (c == ':') result.add(0x3A);
                else if (c == '(') {
                    int value = 0;
                    for (c = s.charAt(++pos); c != ')'; c = s.charAt(++pos)) {
                        value = 16 * value + Character.digit(c, 16);
                    }
                    result.add(value);
                }
                else {
                    log.error("illegal.escape", c);
                }
            } else {
                result.add((int)c);
            }
        }
        return result;
    }
}
