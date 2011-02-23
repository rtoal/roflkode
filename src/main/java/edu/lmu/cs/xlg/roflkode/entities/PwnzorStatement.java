package edu.lmu.cs.xlg.roflkode.entities;

/**
 * The statement of the form 'PWNZOR v' for variable v.
 */
public class PwnzorStatement extends Statement {

    private VariableExpression target;

    public PwnzorStatement(VariableExpression target) {
        this.target = target;
    }

    public VariableExpression getTarget() {
        return target;
    }
}
