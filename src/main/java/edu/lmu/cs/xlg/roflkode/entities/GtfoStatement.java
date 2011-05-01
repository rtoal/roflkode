package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode GTFO statement.  This can serve to exit a target or to exit a function that does not
 * return a value.
 */
public class GtfoStatement extends Statement {

    private String targetName;
    private Entity target;

    public GtfoStatement(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public Entity getTarget() {
        return target;
    }

    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {

        // Try first to see if it is return from the current function
        if (function != null && function.getName().equals(targetName)) {
            target = table.lookupFunction(targetName, log);
            if (target != null) {
                // Found a function, it better not have a return type
                if (Function.class.cast(target).getReturnType() != null) {
                    log.error("missing.return.value");
                }
                return;
            }
        }

        // It wasn't the current function, so it must be a loop
        if (!inLoop) {
            log.error("gtfo.not.in.loop");
        } else {
            target = table.lookupLoop(targetName, log);
        }
    }
}
