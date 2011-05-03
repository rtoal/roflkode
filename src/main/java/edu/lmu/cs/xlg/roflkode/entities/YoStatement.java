package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode Yo statement.
 */
public class YoStatement extends Statement {

    private List<Expression> expressions;

    public YoStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    /**
     * Analyzes the statement.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        for (Expression a: expressions) {
            a.analyze(log, table, function, inLoop);
        }
    }
}
