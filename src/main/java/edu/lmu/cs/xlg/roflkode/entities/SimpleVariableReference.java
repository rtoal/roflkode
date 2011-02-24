package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A variable used in an expression which simply has a name.  It is not made up of an
 * array index or property lookup.  It's just, syntactically, a name.
 */
public class SimpleVariableReference extends VariableExpression {

    private String name;

    /**
     * Creates a variable reference.
     */
    public SimpleVariableReference(String name) {
        this.name = name;
    }

    /**
     * Returns the name.
     */
    public String getName() {
        return name;
    }
}
