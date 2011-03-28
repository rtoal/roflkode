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
     * Returns true, because simple variables are always writable in Roflkode.
     */
    public boolean isWritable() {
       return true;
    }
}
