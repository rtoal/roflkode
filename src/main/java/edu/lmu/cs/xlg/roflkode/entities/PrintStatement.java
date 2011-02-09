package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode print statement. This statement prints a sequence of expressions to standard output.
 */
public class PrintStatement extends Statement {

    List<Expression> expressions;

    public PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
