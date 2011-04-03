package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode GTFO statement.  This is like a "break" statement in many popular languages.
 * In Roflkode, the statement may have an optional loop name operand.
 */
public class GtfoStatement extends Statement {

    private String loopName;
    private LoopStatement loop;

    public GtfoStatement(String loopName) {
        this.loopName = loopName;
    }

    public String getLoopName() {
        return loopName;
    }

    public LoopStatement getLoop() {
        return loop;
    }

    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        if (!inLoop) {
            log.error("gtfo_not_in_loop");
        }
        if (loopName != null) {
            loop = table.lookupLoop(loopName, log);
        }
    }
}
