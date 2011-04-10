package edu.lmu.cs.xlg.roflkode.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
System.out.println("Analyzing array exp");
        // First analyze the subexpressions
        for (Expression e: expressions) {
            e.analyze(log, table);
        }

        // Now lets get the type of the array expression. Begin by creating a set of all of the
        // expressions in the array itself.
        Set<Type> types = new HashSet<Type>();
        for (Expression e: expressions) {
            types.add(e.getType());
            System.out.println("Adding " + e.getType());
        }

        // Now see if we can make any sense out of the array of types.
        if (types.isEmpty()) {
            type = Type.ARBITRARY_ARRAY;

        } else if (types.size() == 1) {
            if (types.contains(Type.N00B_TYPE)) {
                // Nothing but n00bs in the array
                type = Type.ARBITRARY_ARRAY_OF_REFERENCES;
            } else {
                // Exactly one type represented -- good deal
                type = types.toArray(new Type[0])[0].array();
            }

        } else if (types.size() == 2 && types.contains(Type.NUMBR) && types.contains(Type.INT)) {
            // Only NUMBRs and INTs -- okay too
            type = Type.NUMBR.array();

        } else {
            // Can't make sense out of it because of conflicts
            log.error("bad.array.expression");
            type = Type.ARBITRARY;
        }
    }
}
