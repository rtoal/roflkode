package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

public class YoStatement extends Statement {

    private List<Expression> expressions;

    public YoStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
