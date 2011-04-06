package edu.lmu.cs.xlg.roflkode.entities;

import java.util.Iterator;
import java.util.List;

import edu.lmu.cs.xlg.util.Log;

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

    /**
     * Analyzes the bukkit constructor expression.
     */
    public void analyze(Log log, SymbolTable table) {
        type = table.lookupType(typename, log);
        if (! (type instanceof BukkitType)) {
            log.error("not.a.bukkit.type", type.getName());
            return;
        }

        List<BukkitType.Property> properties = BukkitType.class.cast(type).getProperties();

        if (args.size() != properties.size()) {
            log.error("wrong.number.of.properties", type.getName(), properties.size(), args.size());
            return;
        }

        Iterator<Expression> ai = args.iterator();
        Iterator<BukkitType.Property> fi = properties.iterator();
        while (ai.hasNext()) {
            Expression a = ai.next();
            BukkitType.Property f = fi.next();
            a.analyze(log, table);
            a.assertAssignableTo(f.getType(), log, "property.type.error");
        }
    }
}
