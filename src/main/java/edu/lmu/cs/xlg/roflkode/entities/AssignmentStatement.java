package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode assignment statement.
 */
public class AssignmentStatement extends Statement {

    private VariableExpression left;
    private Expression right;

    /**
     * Creates an assignment statement.
     */
    public AssignmentStatement(VariableExpression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the destination of the assignment.
     */
    public VariableExpression getLeft() {
        return left;
    }

    /**
     * Returns the source of the assignment.
     */
    public Expression getRight() {
        return right;
    }
}
