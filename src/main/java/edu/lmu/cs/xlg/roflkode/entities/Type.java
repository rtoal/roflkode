package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode type.
 */
public class Type extends Entity {

    public static final Type INT = new Type("INT");
    public static final Type NUMBR = new Type("NUMBR");
    public static final Type B00L = new Type("B00L");
    public static final Type KAR = new Type("KAR");
    public static final Type YARN = new Type("YARN");

    private String name;

    /**
     * Constructs a type with the given name.
     */
    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
