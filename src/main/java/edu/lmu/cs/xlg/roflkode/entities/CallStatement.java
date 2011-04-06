package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode call statement.  This consists of a function, which must not have a return type,
 * being applied to zero or more arguments, in a statement.
 */
public class CallStatement extends Statement {

    private String functionName;
    private List<Expression> arguments;
    private Function function;

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

    public Function getFunction() {
        return function;
    }

    /**
     * Analyzes the call.
     */
    public void analyze(Log log, SymbolTable table, Function owner, boolean inLoop) {

        // Analyze arguments first
        for (Expression a: arguments) {
            a.analyze(log, table);
        }

        // Find out which function we're referring to.
        function = table.lookupFunction(functionName, log);

        // Ensure it is void
        if (function != null && function.getReturnType() != null) {
            log.error("non.void.function.in.statement", functionName);
        }
    }
}
