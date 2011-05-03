package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

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

    /**
     * Analyzes this statement.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        if (function == null) {
            // At top-level, not inside any function
            log.error("return.outside.function");

        } else if (function.getReturnType() == null) {
            // Inside a procedure, we can't have a HEREZ statement
            if (expression != null) {
                log.error("return.value.not.allowed");
            }

        } else {
            // Returning something from a function, so typecheck
            expression.analyze(log, table, function, inLoop);
            expression.assertAssignableTo(function.getReturnType(), log, "return.type.error");
        }
    }
}
