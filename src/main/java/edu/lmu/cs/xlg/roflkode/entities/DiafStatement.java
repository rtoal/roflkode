package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * The Roflkode DIAF statement, which throws a optional string-valued expression.
 */
public class DiafStatement extends Statement {

    private Expression expression;

    public DiafStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        if (expression != null) {
            expression.analyze(log, table);
            expression.assertString("DIAF", log);
        }
    }
}
