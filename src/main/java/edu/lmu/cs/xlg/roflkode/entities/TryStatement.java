package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * The PLZ ... AWSUM THX ... O NOES ... MKAY statement.
 */
public class TryStatement extends Statement {

    private Statement statementToTry;
    private Block successBlock;
    private Block failBlock;

    public TryStatement(Statement statementToTry, Block successBlock, Block failBlock) {
        this.statementToTry = statementToTry;
        this.successBlock = successBlock;
        this.failBlock = failBlock;
    }

    public Statement getStatementToTry() {
        return statementToTry;
    }

    public Block getSuccessBlock() {
        return successBlock;
    }

    public Block getFailBlock() {
        return failBlock;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        statementToTry.analyze(log, table, function, inLoop);
        successBlock.analyze(log, table, function, inLoop);
        failBlock.analyze(log, table, function, inLoop);
    }
}
