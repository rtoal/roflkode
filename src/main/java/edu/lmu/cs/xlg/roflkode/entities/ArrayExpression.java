package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode array expression, for example
 * <pre>
 *     [: 3 5.6 222 8 count :]
 * </pre>
 */
public class ArrayExpression extends Expression {

    private List<Expression> expressions;

    /**
     * Creates an array expression.
     */
    public ArrayExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Returns the list of expressions used to initialize the array.
     */
    public List<Expression> getExpressions() {
        return expressions;
    }

    /**
     * Analyzes the array (constructor) expression.  We get to infer the type of the array
     * based on the types of the components.
     */
    public void analyze(Log log, SymbolTable table) {

        // First analyze the subexpressions
        for (Expression e: expressions) {
            e.analyze(log, table);
        }

        // Now make sure all the subexpressions are compatible with each other's types.
        // TODO

        // Set the type of the whole array to be the array type of the most general expression.
        type = Type.ARBITRARY; // TODO!!!! NOT DONE YET!!
    }

}
