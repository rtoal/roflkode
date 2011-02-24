package edu.lmu.cs.xlg.roflkode.entities;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Superclass for all entities in the compiler.
 *
 * The front end of the compiler produces an abstract syntax tree which is then decorated (or
 * transformed) into a semantic graph. The nodes this structure are called entities. The hierarchy
 * for entities will look something like this:
 *
 * <pre>
 * Entity
 *    Block
 *        Script
 *    Variable
 *    Type
 *       ArrayType
 *       BukkitType
 *    Function
 *    Statement
 *       Declaration
 *       IncrementStatement
 *       CallStatement
 *       AssignmentStatement
 *       BreakStatement
 *       AgainStatement
 *       ReturnStatement
 *       YoStatement
 *       FacepalmStatement
 *       IfStatement
 *       SwitchStatement
 *       WhileStatement
 *       ForStatement
 *    Expression
 *       Literal
 *          IntegerLiteral
 *          NumberLiteral
 *          BoolLiteral
 *          CharacterLiteral
 *          StringLiteral
 *       VariableExpression
 *          SimpleVariableReference
 *          ArraySlot
 *          BukkitSlot
 *          Application
 *       UnaryExpression
 *       BinaryExpression
 *       ArrayExpression
 *       BukkitExpression
 * </pre>
 *
 * Each concrete entity class has a constructor to fill in the "syntactic" part of the entity. For
 * example, we know the name of a variable reference while generating the abstract syntax tree, so
 * that is filled in by the constructor. However, we don't know until semantic analysis exactly
 * which variable is being referred to, so that field is not filled in by the constructor.
 */
public abstract class Entity {

    /**
     * Collection of all entities that have ever been created, as a map with the entities as keys
     * and their ids as values.
     */
    private static Map<Entity, Integer> all = new LinkedHashMap<Entity, Integer>();

    /**
     * Creates an entity, "assigning" it a new unique id by placing it in a global map mapping the
     * entity to its id.
     */
    public Entity() {
        synchronized (all) {
            all.put(this, all.size());
        }
    }

    /**
     * Returns a short string containing this entity's id.
     */
    public String toString() {
        return "$e" + all.get(this);
    }

    /**
     * Writes a description of all entities reachable from the given entity, one per line, to the
     * given writer. The description includes its id, class name, and all non-static attributes with
     * non-null values.
     */
    public static void dump(PrintWriter writer, Entity e) {
        Set<Entity> used = new HashSet<Entity>();
        dump(writer, e, used);
        // dump(writer, Type.ARBITRARY, used);
        // dump(writer, Type.N00B_TYPE, used);
        // dump(writer, Type.ARRAY_OR_STRING, used);
        // dump(writer, Literal.NULL, used);
        // dump(writer, BooleanLiteral.FALSE, used);
        // dump(writer, BooleanLiteral.TRUE, used);
        // dump(writer, Variable.ARBITRARY, used);
    }

    /**
     * Helper to do the actual writing and entity graph traversal.
     */
    private static void dump(PrintWriter writer, Entity e, Set<Entity> used) {
        if (used.contains(e)) {
            return;
        }
        used.add(e);

        String classname = e.getClass().getName();
        String kind = classname.substring(classname.lastIndexOf('.') + 1);
        writer.print(e + "\t[" + kind + "]");
        List<Field> fields = relevantFields(e.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            if ((field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            try {
                String name = field.getName();
                Object value = field.get(e);
                if (value == null) {
                    continue;
                }
                if (value.getClass().isArray()) {
                    value = Arrays.asList((Object[]) value);
                }
                writer.print(" " + name + "=" + value);
            } catch (IllegalAccessException cannotHappen) {
            }
        }
        writer.println();

        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;
            try {
                Object value = field.get(e);
                if (value == null) {
                    continue;
                }
                if (value instanceof Entity) {
                    dump(writer, (Entity) value, used);
                } else if (value.getClass().isArray()) {
                    dump(writer, Arrays.asList((Object[]) value), used);
                } else if (value instanceof Collection<?>) {
                    dump(writer, (Collection<?>) value, used);
                } else if (value instanceof Map<?, ?>) {
                    dump(writer, ((Map<?, ?>) value).values(), used);
                }
            } catch (IllegalAccessException cannotHappen) {
            }
        }
    }

    /**
     * Writes information for all entities in the collection iterated over by the given iterator.
     */
    private static void dump(PrintWriter writer, Iterable<?> i, Set<Entity> used) {
        for (Object object : i) {
            if (object instanceof Entity) {
                dump(writer, (Entity) object, used);
            }
        }
    }

    /**
     * Returns a list of all non-private fields of class c, together with fields of its ancestor
     * classes, assuming that c is a descendant class of Entity.
     */
    private static List<Field> relevantFields(Class<?> c) {
        List<Field> attributes = new ArrayList<Field>();
        attributes.addAll(Arrays.asList(c.getDeclaredFields()));
        if (c.getSuperclass() != Entity.class) {
            attributes.addAll(relevantFields(c.getSuperclass()));
        }
        return attributes;
    }
}
