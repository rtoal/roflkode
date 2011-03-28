package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode variable declaration.
 */
public class Variable extends Declaration {

    private String typename;
    private Expression initializer;
    private Type type;

    /**
     * An arbitrary variable, useful in semantic analysis to take the place of a variable that has
     * not been declared.  This variable is type-compatible with everything, so its use serves to
     * prevent a flood of spurious error messages.
     */
    public static final Variable ARBITRARY = new Variable("<unknown>", Type.ARBITRARY);

    /**
     * Constructs a variable.
     */
    public Variable(String name, String typename, Expression initializer) {
        super(name);
        this.typename = typename;
        this.initializer = initializer;
    }

    /**
     * Special constructor for variables created during semantic analysis (not known while parsing).
     * Note that this takes in a real type, rather than just a type name, because these variables
     * aren't part of a user's code and don't have to get analyzed.  TODO: Maybe there is a way
     * to handle these normally.
     */
    public Variable(String name, Type type) {
        super(name);
        this.typename = type.getName();
        this.initializer = null;
        this.type = type;
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

    /**
     * Returns the type of this variable.
     */
    public Type getType() {
        return type;
    }

    /**
     * Analyzes this variable.
     */
    public void analyze(Log log, SymbolTable table, Function owner, boolean inLoop) {
        type = table.lookupType(typename, log);

        // If an initializer is present, analyze it and check types.
        if (initializer != null) {
            initializer.analyze(log, table);
            initializer.assertAssignableTo(type, log, "init_type_error");
        }
    }
}
