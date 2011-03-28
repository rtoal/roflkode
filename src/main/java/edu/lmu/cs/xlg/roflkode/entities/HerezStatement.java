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
            log.error("return_outside_function");

        } else if (owner.getReturnType() == null) {
            // Inside a procedure, better not have a return expression
            if (expression != null) {
                log.error("return_value_not_allowed");
            }

        } else if (expression == null) {
            // Inside a function without a return expression
            log.error("return_value_required");

        } else {
            // Returning something from a function, so typecheck
            expression.analyze(log, table);
            expression.assertAssignableTo(owner.getReturnType(), log, "return_type_error");
        }
    }
}
