package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode type.
 */
public class Type extends Declaration {

    public static final Type INT = new Type("INT");
    public static final Type NUMBR = new Type("NUMBR");
    public static final Type B00L = new Type("B00L");
    public static final Type KAR = new Type("KAR");
    public static final Type YARN = new Type("YARN");

    /**
     * A type representing the union of all types.  It is assigned to an entity whose typename is
     * not in scope to allow compilation to proceed without generating too many spurious errors.
     * It is compatible with all other types.
     */
    public static final Type ARBITRARY = new Type("<arbitrary>");

    /**
     * The type whose sole member is the literal N00B.
     */
    public static final Type N00B_TYPE = new Type("<type_of_n00b>");

    /**
     * A type representing the union of the string type and all array types.  This is required
     * because some Roflkode operations work on either strings or arrays.
     */
    public static final Type ARRAY_OR_STRING = new Type("<array_or_string>");

    // The type of arrays of this type.  Created only if needed.
    private ArrayType arrayOfThisType = null;

    /**
     * Constructs a type with the given name.
     */
    Type(String name) {
        super(name);
    }

    /**
     * Returns whether this type is a reference type.
     */
    public boolean isReference() {
        return this == YARN
            || this instanceof ArrayType
            || this instanceof BukkitType
            || this == ARRAY_OR_STRING
            || this == ARBITRARY;
    }

    /**
     * Returns whether this type is an arithmetic type.
     */
    public boolean isArithmetic() {
        return this == INT || this == NUMBR;
    }

    /**
     * Returns the type that is an array of this type, lazily creating it.
     */
    public Type array() {
        if (arrayOfThisType == null) {
            arrayOfThisType = new ArrayType(this);
        }
        return arrayOfThisType;
    }

    /**
     * A default implementation that does nothing, since many type subclasses need no analysis.
     */
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
    }
}
