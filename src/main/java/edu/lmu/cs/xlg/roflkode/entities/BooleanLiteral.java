package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A simple typsafe enum of the two boolean literals, WIN and FAIL.
 */
public class BooleanLiteral extends Literal {

    public static final BooleanLiteral WIN = new BooleanLiteral("WIN");
    public static final BooleanLiteral FAIL = new BooleanLiteral("FAIL");

    /**
     * Constructs a boolean literal.  The constructor is private because the only two instances
     * of this class are defined as public static members.
     */
    private BooleanLiteral(String lexeme) {
        super(lexeme);
    }
}
