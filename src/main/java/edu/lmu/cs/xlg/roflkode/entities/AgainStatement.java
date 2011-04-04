package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode HWGA statement.  This is like a "continue" statement in many popular languages.
 * In Roflkode, the statement may have an optional loop name operand.
 */
public class AgainStatement extends Statement {

    private String loopName;
    private LoopStatement loop;

    public AgainStatement(String loopName) {
        this.loopName = loopName;
    }

    public String getLoopName() {
        return loopName;
    }

    /**
     * Returns the loop statement that this statement is trying to continue.  This is not
     * known during syntax checking or AST generation.  It is only computed during semantic
     * analysis, where we look up the name.
     */
    public LoopStatement getLoop() {
        return loop;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        if (!inLoop) {
            log.error("hwga.not.in.loop");
        }
        if (loopName != null) {
            loop = table.lookupLoop(loopName, log);
        }
    }
}
