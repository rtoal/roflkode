package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode declaration.  Declarations appear as statements, and declare a variable, type, or
 * functions.
 */
public abstract class Declaration extends Statement {

    private String name;

    public Declaration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
