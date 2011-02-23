package edu.lmu.cs.xlg.roflkode.entities;

public class SubscriptedVariable extends VariableExpression {

    private VariableExpression array;
    private Expression index;

    /**
     * Creates a subscripted variable.
     */
    public SubscriptedVariable(VariableExpression v, Expression i) {
        this.array = v;
        this.index = i;
    }

    /**
     * Returns the array.
     */
    public VariableExpression getArray() {
        return array;
    }

    /**
     * Returns the index.
     */
    public Expression getIndex() {
        return index;
    }
}
