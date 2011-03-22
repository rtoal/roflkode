package edu.lmu.cs.xlg.roflkode.entities;

public class Variable extends Entity {

    private String name;
    private String typename;
    private Expression initializer;

    /**
     * Constructs a variable.
     */
    public Variable(String name, String typename, Expression initializer) {
        this.name = name;
        this.typename = typename;
        this.initializer = initializer;
    }

    /**
     * Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the initializer.
     */
    public Expression getInitializer() {
        return initializer;
    }

    /**
     * Returns the typename.
     */
    public String getTypename() {
        return typename;
    }
}
