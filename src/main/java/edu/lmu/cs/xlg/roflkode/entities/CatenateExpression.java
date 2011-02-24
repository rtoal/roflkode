package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode concatenate expression.  Its purpose is to concatenate a list of strings.
 */
public class CatenateExpression extends Expression {

    private List<Expression> expressions;

    /**
     * Constructs a catenate expression.
     */
    public CatenateExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Returns the list of expressions that are to be catenated.
     */
    public List<Expression> getExpressions() {
        return expressions;
    }
}
