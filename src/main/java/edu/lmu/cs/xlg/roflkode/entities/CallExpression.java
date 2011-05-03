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
     * Returns the arguments
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
     * Analyzes the call.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {

        // Analyze all the arguments first.
        for (Expression a: args) {
            a.analyze(log, table, function, inLoop);
        }

        // Find out which function we're calling.
        function = table.lookupFunction(functionName, log);

        // If we can't find the function, just forget it.  The lookupFunction call above
        // will have already logged the error.  We just want to bail now and not bother
        // checking the return type and the arguments.
        if (function == null) {
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

        // Now check all the arguments against all the parameters.
        function.assertCanBeCalledWith(args, log);
    }

    /**
     * Returns false, as one can never write to a function call result.
     */
    public boolean isWritable() {
        return false;
    }
}
