package edu.lmu.cs.xlg.roflkode.entities;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode variable of the form v!!!p for bukkit variable expression v and property p.
 */
public class PropertyVariableExpression extends VariableExpression {

    private VariableExpression bukkit;
    private String propertyName;
    private BukkitType.Property property;

    /**
     * Creates a variable.
     */
    public PropertyVariableExpression(VariableExpression bukkit, String propertyName) {
        this.bukkit = bukkit;
        this.propertyName = propertyName;
    }

    /**
     * Returns the property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the bukkit.
     */
    public VariableExpression getBukkit() {
        return bukkit;
    }

    /**
     * Returns the property.
     */
    public BukkitType.Property getProperty() {
        return property;
    }

    /**
     * Analyzes this variable, checking that the variable expression before the dot has a type that
     * is a bukkit; that the property exists, etc.
     */
    public void analyze(Log log, SymbolTable table, Function function, boolean inLoop) {
        bukkit.analyze(log, table, function, inLoop);

        if (!(bukkit.type instanceof BukkitType)) {
            log.error("not.a.bukkit");
            type = Type.ARBITRARY;
        } else {
            property = ((BukkitType)bukkit.type).getProperty(propertyName, log);

            // The type of this variable is the type of the property.
            type = property.getType();
        }
    }

    /**
     * Returns true, as one can always write to this kind of variable in Roflkode.  In other
     * languages we might allow for read-only fields, but not here.
     */
    public boolean isWritable() {
        return true;
    }
}
