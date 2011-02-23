package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

public class FacepalmStatement extends Statement {

    private List<Expression> expressions;

    public FacepalmStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
