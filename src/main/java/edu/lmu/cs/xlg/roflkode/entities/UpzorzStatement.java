package edu.lmu.cs.xlg.roflkode.entities;

/**
 * The statement of the form 'UPZORZ v' for variable v.
 */
public class UpzorzStatement extends Statement {

    private VariableExpression target;

    public UpzorzStatement(VariableExpression target) {
        this.target = target;
    }

    public VariableExpression getTarget() {
        return target;
    }
}
