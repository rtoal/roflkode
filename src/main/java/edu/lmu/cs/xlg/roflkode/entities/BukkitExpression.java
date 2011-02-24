package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A expression directly constructing a new instance of a bukkit type. Examples:
 * <ul>
 * <li><code>point &lt;: 3 4 :></code>
 * <li><code>person &lt;: 1536277 "Alice" (makeDate "2001-01-05") :></code>
 * <li><code>amount &lt;: 2.95  "USD" :></code>
 * </ul>
 */
public class BukkitExpression extends Expression {

    private String typename;
    private List<Expression> args;

    /**
     * Constructs a bukkit expression.
     */
    public BukkitExpression(String typename, List<Expression> args) {
        this.typename = typename;
        this.args = args;
    }

    /**
     * Returns the typename.
     */
    public String getTypename() {
        return typename;
    }

    /**
     * Returns the args.
     */
    public List<Expression> getArgs() {
        return args;
    }
}
