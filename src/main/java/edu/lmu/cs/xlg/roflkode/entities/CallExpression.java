package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

public class CallExpression extends VariableExpression {

    private String functionName;
    private List<Expression> args;

    /**
     * Creates the function call expression.
     */
    public CallExpression(String functionName, List<Expression> args) {
        this.functionName = functionName;
        this.args = args;
    }

    /**
     * Returns the args.
     */
    public List<Expression> getArgs() {
        return args;
    }

    /**
     * Returns the function name.
     */
    public String getFunctionName() {
        return functionName;
    }
}
