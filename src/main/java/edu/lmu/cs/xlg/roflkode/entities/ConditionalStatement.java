package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

public class ConditionalStatement extends Statement {

    public static class Arm {
        Expression guard;
        Block block;
        public Arm(Expression guard, Block block) {
            this.guard = guard;
            this.block = block;
        }
    }

    private List<Arm> arms;
    private Block elsePart;

    public ConditionalStatement(List<Arm> arms, Block elsePart) {
        this.arms = arms;
        this.elsePart = elsePart;
    }

    public List<Arm> getArms() {
        return arms;
    }

    public Block getElsePart() {
        return elsePart;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        // TODO Auto-generated method stub
    }
}
