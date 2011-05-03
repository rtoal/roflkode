package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A variable expression composed simply of a name.  It is not made up of an array index or
 * property lookup.  It's just, syntactically, a name.
 */
public class SimpleVariableExpression extends VariableExpression {

    private String name;
    private Variable referent;
    private Boolean nonLocal;

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

    /**
     * Returns the referent.
     */
    public Variable getReferent() {
        return referent;
    }

    /**
     * Returns whether this variable reference is of a variable not declared in the function
     * containing this reference.
     */
    public boolean isNonLocal() {
        return nonLocal == null ? false : nonLocal.booleanValue();
    }

    /**
     * Analyzes the variable expression.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        referent = table.lookupVariable(name, log);
        type = referent.getType();
        int functionLevel = function == null ? 0 : function.getLevel();
        nonLocal = functionLevel != referent.getLevel();
    }

    /**
     * Returns whether the simple variable expression is writable, which it will be if and only
     * if its referent is not marked constant.
     */
    public boolean isWritable() {
       return !referent.isConstant();
    }
}
