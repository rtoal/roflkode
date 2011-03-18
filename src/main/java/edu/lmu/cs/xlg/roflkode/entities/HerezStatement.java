package edu.lmu.cs.xlg.roflkode.entities;

/**
 * The "HEREZ UR" statement of Roflkode.
 */
public class HerezStatement extends Statement {

    private Expression expression;

    public HerezStatement(Expression expression) {
        this.expression = expression;
    }

    /**
     * Returns the expression.
     */
    public Expression getExpression() {
        return expression;
    }
}
