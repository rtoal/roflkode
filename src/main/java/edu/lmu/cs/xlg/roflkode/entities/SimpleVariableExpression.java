package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A variable expression composed simply of a name.  It is not made up of an array index or
 * property lookup.  It's just, syntactically, a name.
 */
public class SimpleVariableExpression extends VariableExpression {

    private String name;

    /**
     * Creates a simple variable expression.
     */
    public SimpleVariableExpression(String name) {
        this.name = name;
    }

    /**
     * Returns the name.
     */
    public String getName() {
        return name;
    }
}
