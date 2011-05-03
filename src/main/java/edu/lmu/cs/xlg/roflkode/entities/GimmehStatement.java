package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * The statement of the form 'GIMMEH v' for variable v.
 */
public class GimmehStatement extends Statement {

    private VariableExpression target;

    public GimmehStatement(VariableExpression target) {
        this.target = target;
    }

    public VariableExpression getTarget() {
        return target;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        target.analyze(log, table, function, inLoop);
        target.assertWritable(log);
    }
}
