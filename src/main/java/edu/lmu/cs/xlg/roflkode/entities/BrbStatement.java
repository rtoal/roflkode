package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode BRB statement.
 */
public class BrbStatement extends Statement {

    private Expression expression;

    public BrbStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        expression.analyze(log, table, function, inLoop);
        expression.assertArithmetic("BRB", log);
    }
}
