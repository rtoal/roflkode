package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A singleton class for an object representing the literal N00B.
 */
public class NoobLiteral extends Literal {

    public static NoobLiteral INSTANCE = new NoobLiteral();

    // Constructor is private because this class is a singleton.
    private NoobLiteral() {
        super("N00B");
    }
}
