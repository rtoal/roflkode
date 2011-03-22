package edu.lmu.cs.xlg.roflkode.entities;

public class DiafStatement extends Statement {

    private Expression expression;

    public DiafStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
