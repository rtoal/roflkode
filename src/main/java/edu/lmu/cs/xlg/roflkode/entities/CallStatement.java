package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode call statement.  This consists of a function, which must not have a return type,
 * being applied to zero or more arguments, in a statement.
 */
public class CallStatement extends Statement {

    private String functionName;
    private List<Expression> arguments;

    public CallStatement(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}
