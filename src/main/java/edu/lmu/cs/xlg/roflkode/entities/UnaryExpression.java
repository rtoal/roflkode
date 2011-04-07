package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

public class UnaryExpression extends Expression {

    private String op;
    private Expression operand;

    /**
     * Creates a unary expression.
     */
    public UnaryExpression(String op, Expression operand) {
        this.op = op;
        this.operand = operand;
    }

    /**
     * Returns the operator.
     */
    public String getOp() {
        return op;
    }

    /**
     * Returns the operand.
     */
    public Expression getOperand() {
        return operand;
    }

    /**
     * Analyzes this expression.
     */
    public void analyze(Log log, SymbolTable table) {
        operand.analyze(log, table);

        if ("NAA".equals(op)) {
            operand.assertBoolean(op, log);
            type = Type.B00L;

        } else if ("BITZFLIP".equals(op)) {
            operand.assertInteger(op, log);
            type = Type.INT;

        } else if ("SIEZ".equals(op)) {
            operand.assertArrayOrString(op, log);
            type = Type.INT;

        } else if ("B00LZOR".equals(op)) {
            type = Type.B00L;

        } else if ("KARZOR".equals(op)) {
            operand.assertInteger(op, log);
            type = Type.KAR;

        } else if ("INTZOR".equals(op)) {
            operand.assertArithmeticOrChar(op, log);
            type = Type.INT;

        } else if ("YARNZOR".equals(op)) {
            type = Type.YARN;

        } else if ("NUMZOR".equals(op)) {
            operand.assertInteger(op, log);
            type = Type.NUMBR;

        } else {
            log.error("compiler.bug");
            type = Type.ARBITRARY;
        }
    }
}
