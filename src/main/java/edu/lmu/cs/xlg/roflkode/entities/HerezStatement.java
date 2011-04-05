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
    public void analyze(Log log, SymbolTable table, Function owner, boolean inLoop) {
        if (owner == null) {
            // At top-level, not inside any function
            log.error("return.outside.function");

        } else if (owner.getReturnType() == null) {
            // Inside a procedure, we can't have a HEREZ statement
            if (expression != null) {
                log.error("return.value.not.allowed");
            }

        } else {
            // Returning something from a function, so typecheck
            expression.analyze(log, table);
            expression.assertAssignableTo(owner.getReturnType(), log, "return.type.error");
        }
    }
}
