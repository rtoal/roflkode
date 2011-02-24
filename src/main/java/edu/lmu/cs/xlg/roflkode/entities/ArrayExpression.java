package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode array expression, for example
 * <pre>
 *     [: 3 5.6 222 8 count :]
 * </pre>
 */
public class ArrayExpression extends Expression {

    private List<Expression> expressions;

    /**
     * Creates an array expression.
     */
    public ArrayExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Returns the list of expressions used to initialize the array.
     */
    public List<Expression> getExpressions() {
        return expressions;
    }
}
