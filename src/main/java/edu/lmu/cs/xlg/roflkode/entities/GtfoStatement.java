package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode GTFO statement.  This can serve to exit a loop or to exit a function that does not
 * return a value.
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
        //
        //
        //
        // TODO
        //
        //
        //
    }
}
