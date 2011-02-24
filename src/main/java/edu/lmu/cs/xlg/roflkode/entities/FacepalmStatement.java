package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode facepalm statement.
 */
public class FacepalmStatement extends Statement {

    private List<Expression> expressions;

    /**
     * Creates a facepalm statement.
     */
    public FacepalmStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Returns the list of expressions to be written to standard error.
     */
    public List<Expression> getExpressions() {
        return expressions;
    }
}
