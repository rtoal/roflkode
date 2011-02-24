package edu.lmu.cs.xlg.roflkode.entities;

/**
 * The statement of the form 'NERFZORZ v' for variable v.
 */
public class NerfzorzStatement extends Statement {

    private VariableExpression target;

    /**
     * Creates a Nerfzorz statement.
     */
    public NerfzorzStatement(VariableExpression target) {
        this.target = target;
    }

    /**
     * Returns the variable to be nerfzorzed.
     */
    public VariableExpression getTarget() {
        return target;
    }
}
