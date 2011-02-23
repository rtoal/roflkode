package edu.lmu.cs.xlg.roflkode.entities;

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
