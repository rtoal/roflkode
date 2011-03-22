package edu.lmu.cs.xlg.roflkode.entities;

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
}
