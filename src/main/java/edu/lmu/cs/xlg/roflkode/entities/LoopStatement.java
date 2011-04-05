package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode loop statement.
 */
public class LoopStatement extends Statement {

    private String name;
    private String loopType;
    private Expression condition;
    private String iterator;
    private Expression start;
    private Expression end;
    private Expression collection;
    private Block body;

    public LoopStatement(String name, String loopType, Expression condition, String iterator,
            Expression start, Expression end, Expression collection, Block body) {
        this.name = name;
        this.loopType = loopType;
        this.condition = condition;
        this.iterator = iterator;
        this.start = start;
        this.end = end;
        this.collection = collection;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public String getLoopType() {
        return loopType;
    }

    public Expression getCondition() {
        return condition;
    }

    public String getIterator() {
        return iterator;
    }

    public Expression getStart() {
        return start;
    }

    public Expression getEnd() {
        return end;
    }

    public Expression getCollection() {
        return collection;
    }

    public Block getBody() {
        return body;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {

        // TODO - make sure name not already there and then insert it

        // Condition can only exist if type is WHIEL or TIL
        if ("WHIEL".equals(loopType) || "TIL".equals(loopType)) {
            if (condition != null) {
                condition.analyze(log, table);
            }
        }
    }
}
