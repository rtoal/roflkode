package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

public class CallStatement extends Statement {

    private String functionName;
    private List<Expression> expressions;

    public CallStatement(String functionName, List<Expression> expressions) {
        this.functionName = functionName;
        this.expressions = expressions;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
