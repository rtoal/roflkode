package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode switch statement.  Like most languages, it contains a list of one or more arms
 * and an optional else part.
 */
public class SwitchStatement extends Statement {

    public static class Arm {
        private Literal guard;
        private Block block;

        public Arm(Literal guard, Block block) {
            this.guard = guard;
            this.block = block;
        }
    }

    private Expression expression;
    private List<Arm> arms;
    private Block elsePart;

    public SwitchStatement(Expression expression, List<Arm> arms, Block elsePart) {
        this.expression = expression;
        this.arms = arms;
        this.elsePart = elsePart;
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Arm> getArms() {
        return arms;
    }

    public Block getElsePart() {
        return elsePart;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        expression.analyze(log, table);
        for (Arm arm: arms) {
            arm.guard.analyze(log, table);
            // TODO - CHECK TYPE COMPATIBILITY BETWEEN arm.guard.type and expression.type
            arm.block.analyze(log, table, function, inLoop);
        }
        if (elsePart != null) {
            elsePart.analyze(log, table, function, inLoop);
        }
    }
}
