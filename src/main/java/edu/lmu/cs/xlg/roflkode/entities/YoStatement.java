package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

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
    public void analyze(Log log, SymbolTable table, Function owner, boolean inLoop) {
        for (Expression a: expressions) {
            a.analyze(log, table);
        }
    }
}
