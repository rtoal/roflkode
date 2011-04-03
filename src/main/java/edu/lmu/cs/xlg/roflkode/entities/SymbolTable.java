package edu.lmu.cs.xlg.roflkode.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.lmu.cs.xlg.util.Log;

public class SymbolTable {

    // The actual contents of the symbol table.  Names map to entities.
    Map<String, Entity> map = new HashMap<String, Entity>();

    // The table to look in if you can't find what you want here.
    SymbolTable parent;

    /**
     * Creates a symbol table with the given parent.
     */
    public SymbolTable(SymbolTable parent) {
        this.map = new HashMap<String, Entity>();
        this.parent = parent;
    }

    /**
     * Inserts an item into the table, rejecting duplicates.
     */
    public void insert(Declaration d, Log log) {
        Object oldValue = map.put(d.getName(), d);

        if (oldValue != null) {
            log.error("redeclared_identifier", d.getName());
        }
    }

    /**
     * Looks up a type in this table, or if not found, searches along its ancestor chain.
     *
     * @param name the name of the type being searched for.
     * @return the innermost visible type with that name.  If not found, or if the value found is
     * not a type object, logs an error message and returns Type.ANY.
     */
    public Type lookupType(String name, Log log) {
        if (name.endsWith(" LIST")) {
            return lookupType(name.substring(0,name.length() - 5), log).array();
        }

        Object value = map.get(name);
        if (value == null) {
            if (parent == null) {
                log.error("type_not_found", name);
                return Type.ARBITRARY;
            } else {
                return parent.lookupType(name, log);
            }
        } else if (value instanceof Type) {
            return (Type)value;
        } else {
            log.error("not_a_type", name);
            return Type.ARBITRARY;
        }
    }

    /**
     * Looks up a variable in this table, or if not found, searches along its ancestor chain.
     *
     * @param name the name of the variable being searched for.
     * @return the innermost visible variable with that name.  If not found, or if the value found
     * is not a variable object, logs an error message and returns Variable.ARBITRARY.
     */
    public Variable lookupVariable(String name, Log log) {
        Object value = map.get(name);
        if (value == null) {
            if (parent == null) {
                log.error("variable_not_found", name);
                return Variable.ARBITRARY;
            } else {
                return parent.lookupVariable(name, log);
            }
        } else if (value instanceof Variable) {
            return (Variable)value;
        } else {
            log.error("not_a_variable", name);
            return Variable.ARBITRARY;
        }
    }

    /**
     * Looks up a function in this table, or if not found, searches along its ancestor chain.
     *
     * @param name the name of the function to search for.
     * @return the innermost visible function with that name.  If not found, or if the value found
     * is not a function object, logs an error message and returns null.
     *
     * TODO: We need Function.ARBITRARY, I think?
     */
    public Function lookupFunction(String name, Log log) {
        Object value = map.get(name);

        if (value == null) {
            if (parent == null) {
                log.error("function_not_found", name);
                return null;
            } else {
                return parent.lookupFunction(name, log);
            }

        } else if (value instanceof Function) {
            return (Function)value;
        } else {
            log.error("not_a_function", name);
            return null;
        }
    }

    public LoopStatement lookupLoop(String loopName, Log log) {
        // TODO
        return null;
    }

    /**
     * Returns all the entities in this symbol table that are instances of class c or any descendant
     * of c.  To get all the entities in the table, pass in class Entity (or java.lang.Object).
     */
    public Set<Object> getEntitiesByClass(Class<?> c) {
        Set<Object> result = new HashSet<Object>();
        for (Object value: map.values()) {
            if (c.isInstance(value)) {
                result.add(value);
            }
        }
        return result;
    }
}
