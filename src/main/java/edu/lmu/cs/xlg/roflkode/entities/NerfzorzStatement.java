package edu.lmu.cs.xlg.roflkode.entities;

/**
 * The statement of the form 'NERFZORZ v' for variable v.
 */
public class NerfzorzStatement extends Statement {

    private VariableExpression target;

    public NerfzorzStatement(VariableExpression target) {
        this.target = target;
    }

    public VariableExpression getTarget() {
        return target;
    }
}
