package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode expression.
 */
public abstract class Expression extends Entity {

    // As Roflkode is statically typed, we can compute and store the type at compile time.
    Type type;

    /**
     * Returns the type of this expression.
     */
    public Type getType() {
        return type;
    }

    /**
     * Performs semantic analysis on the expression.
     */
    public abstract void analyze(Log log, SymbolTable table);

    /**
     * Returns whether this expression is compatible with (that is, "can be assigned to an object
     * of") a given type.
     */
    public boolean isCompatibleWith(Type testType) {
        return this.type == testType
            || this.type == Type.INT && testType == Type.NUMBR
            || this.type == Type.N00B_TYPE && testType.isReference()
            || this.type == Type.ARBITRARY
            || testType == Type.ARBITRARY;
    }

    // Helpers for semantic analysis, called from the analyze methods of other expressions.  These
    // are by no means necessary, but they are very convenient.

    void assertAssignableTo(Type otherType, Log log, String errorKey) {
        if (!this.isCompatibleWith(otherType)) {
            log.error(errorKey, otherType.getName(), this.type.getName());
        }
    }

    void assertArithmetic(String context, Log log) {
        if (!(type == Type.INT || type == Type.NUMBR)) {
            log.error("non.arithmetic", context);
        }
    }

    void assertArithmeticOrChar(String context, Log log) {
        if (!(type == Type.INT || type == Type.NUMBR || type == Type.KAR)) {
            log.error("non.arithmetic.or.char", context);
        }
    }

    void assertInteger(String context, Log log) {
        if (!(type == Type.INT)) {
            log.error("non.integer", context);
        }
    }

    void assertBoolean(String context, Log log) {
        if (!(type == Type.B00L)) {
            log.error("non.boolean", context);
        }
    }

    void assertChar(String context, Log log) {
        if (!(type == Type.KAR)) {
            log.error("non.char", context);
        }
    }

    void assertArray(String context, Log log) {
        if (!(type instanceof ArrayType)) {
            log.error("non.array", context);
        }
    }

    void assertString(String context, Log log) {
        if (!(type == Type.YARN)) {
            log.error("non.string", context);
        }
    }

    void assertArrayOrString(String context, Log log) {
        if (!(type == Type.YARN || type instanceof ArrayType)) {
            log.error("non.array.or.string", context);
        }
    }
}
