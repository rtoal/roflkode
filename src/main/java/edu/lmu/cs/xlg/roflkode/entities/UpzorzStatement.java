package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * The statement of the form 'UPZORZ v' for variable v.
 */
public class UpzorzStatement extends Statement {

    private VariableExpression target;

    public UpzorzStatement(VariableExpression target) {
        this.target = target;
    }

    public VariableExpression getTarget() {
        return target;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        target.analyze(log, table, function, inLoop);
        target.assertInteger("UPZORZ", log);
        target.assertWritable(log);
    }
}
