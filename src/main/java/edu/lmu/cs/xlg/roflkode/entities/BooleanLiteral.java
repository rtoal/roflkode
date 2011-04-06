package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

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

    /**
     * Does nothing, since analysis always succeeds.
     */
    public void analyze(Log log, SymbolTable table) {
        this.type = Type.B00L;
    }
}
