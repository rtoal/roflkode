package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A variable expression composed simply of a name.  It is not made up of an array index or
 * property lookup.  It's just, syntactically, a name.
 */
public class SimpleVariableExpression extends VariableExpression {

    private String name;
    private Variable referent;

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
     * Analyzes the variable expression.
     */
    public void analyze(Log log, SymbolTable table) {
        referent = table.lookupVariable(name, log);
        type = referent.getType();
    }

    /**
     * Returns whether the simple variable expression is writable, which it will be if and only
     * if its referent is not marked constant.
     */
    public boolean isWritable() {
       return !referent.isConstant();
    }
}
