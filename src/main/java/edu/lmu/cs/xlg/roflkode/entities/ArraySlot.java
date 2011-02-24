package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode variable of the form a!!e where a is an array and e is an expression evaluating
 * to an index position within the array.
 */
public class ArraySlot extends VariableExpression {

    private VariableExpression array;
    private Expression index;

    /**
     * Creates a subscripted variable.
     */
    public ArraySlot(VariableExpression v, Expression i) {
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
