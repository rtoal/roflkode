package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A simple statement with a modifier ("IF", "CEPT IF", "WHIEL" or "TIL").
 */
public class ModifiedSimpleStatement extends Statement {

    public static enum Modifier {IF, CEPT, WHIEL, TIL};

    private Modifier modifier;
    private Expression expression;
    private Statement statement;

    public ModifiedSimpleStatement(Modifier modifier, Expression expression, Statement statement) {
        this.modifier = modifier;
        this.expression = expression;
        this.statement = statement;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public Expression getExpression() {
        return expression;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        expression.assertBoolean(modifier.toString(), log);
    }
}
