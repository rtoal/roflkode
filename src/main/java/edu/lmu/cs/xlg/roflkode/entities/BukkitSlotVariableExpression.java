package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode variable of the form v!!!p for bukkit variable expression v and property p.
 */
public class BukkitSlotVariableExpression extends VariableExpression {

    private VariableExpression bukkit;
    private String propertyName;

    /**
     * Creates a variable.
     */
    public BukkitSlotVariableExpression(VariableExpression bukkit, String propertyName) {
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
}
