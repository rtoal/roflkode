package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

public class CallExpression extends VariableExpression {

    private String functionName;
    private List<Expression> args;
    private Function function;

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

    /**
     * Analyzes the call (and its arguments).
     */
    public void analyze(Log log, SymbolTable table) {

        // Analyze all the arguments
        for (Expression a: args) {
            a.analyze(log, table);
        }

        // Find out which function we're calling
        function = table.lookupFunction(functionName, log);

        if (function == null) {
            // If we can't find the function, just forget it
            type = Type.ARBITRARY;
            return;
        }

        // Since called from expression, must have a return type
        if (function.getReturnType() == null) {
            log.error("void.function.in.expression", functionName);
            type = Type.ARBITRARY;
        } else {
            type = function.getReturnType();
        }
    }

    /**
     * Returns false, as one can never write to a function call result.
     */
    public boolean isWritable() {
        return false;
    }
}
