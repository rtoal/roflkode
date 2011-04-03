package edu.lmu.cs.xlg.roflkode.entities;

/**
 * Abstract superclass of all loop statements.
 */
public abstract class LoopStatement extends Statement {

    String name;

    public LoopStatement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
