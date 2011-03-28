package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode HWGA statement.  This is like a "continue" statement in many popular languages.
 * In Roflkode, the statement may have an optional loop name operand.
 */
public class AgainStatement extends Statement {

    private String loopName;

    public AgainStatement(String loopName) {
        this.loopName = loopName;
    }

    public String getLoopName() {
        return loopName;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        // TODO Auto-generated method stub

    }
}
