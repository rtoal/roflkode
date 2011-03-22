package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * The Roflkode Bukkit type.  A bukkit type has a name and a list of properties, where properties
 * are name/type pairs.
 */
public class BukkitType extends Type {

    public static class Property extends Entity {
        private String name;
        private String typename;

        public Property(String name, String typename) {
            this.name = name;
            this.typename = typename;
        }

        public String getName() {
            return name;
        }

        public String getTypename() {
            return typename;
        }
    }

    private List<Property> properties;

    /**
     * Creates a bukkit type.
     */
    public BukkitType(String name, List<Property> properties) {
        super(name);
        this.properties = properties;
    }

    /**
     * Returns the list of properties for this bukkit type.
     */
    public List<Property> getProperties() {
        return properties;
    }
}
