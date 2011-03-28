package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode variable expression of the form a!!e where a is an array and e is an expression
 * evaluating to an index position within the array.
 */
public class IndexVariableExpression extends VariableExpression {

    private VariableExpression array;
    private Expression index;

    /**
     * Creates a subscripted variable.
     */
    public IndexVariableExpression(VariableExpression v, Expression i) {
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


    /**
     * Analyzes this variable expression.
     */
    public void analyze(Log log, SymbolTable table) {
        array.analyze(log, table);
        index.analyze(log, table);

        array.assertArrayOrString("[]", log);
        index.assertInteger("[]", log);
        type = (array.type == Type.YARN) ? Type.KAR
            : array.type instanceof ArrayType ? ArrayType.class.cast(array.type).getBaseType()
            : Type.ARBITRARY;
    }

    /**
     * Array components are writable but string components are not.
     */
    public boolean isWritable() {
        return array.type instanceof ArrayType;
    }
}
