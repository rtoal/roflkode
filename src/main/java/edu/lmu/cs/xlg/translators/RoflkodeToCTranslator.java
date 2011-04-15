package edu.lmu.cs.xlg.translators;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.lmu.cs.xlg.roflkode.entities.ArrayType;
import edu.lmu.cs.xlg.roflkode.entities.BukkitType;
import edu.lmu.cs.xlg.roflkode.entities.BukkitType.Property;
import edu.lmu.cs.xlg.roflkode.entities.Entity;
import edu.lmu.cs.xlg.roflkode.entities.Entity.Visitor;
import edu.lmu.cs.xlg.roflkode.entities.Expression;
import edu.lmu.cs.xlg.roflkode.entities.Function;
import edu.lmu.cs.xlg.roflkode.entities.IndexVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.IntegerLiteral;
import edu.lmu.cs.xlg.roflkode.entities.NoobLiteral;
import edu.lmu.cs.xlg.roflkode.entities.PropertyVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.Script;
import edu.lmu.cs.xlg.roflkode.entities.SimpleVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.Statement;
import edu.lmu.cs.xlg.roflkode.entities.Type;
import edu.lmu.cs.xlg.roflkode.entities.UpzorzStatement;
import edu.lmu.cs.xlg.roflkode.entities.Variable;
import edu.lmu.cs.xlg.roflkode.entities.VariableExpression;

public class RoflkodeToCTranslator {

    /**
     * Writes the C string representation of a Roflkode type.  Notice that YARNs, Bukkits, and
     * arrays are all reference types in Roflkode, so these are all represented as pointer types
     * in C.
     */
    private void translateType(Type type, PrintWriter writer) {
        if (type == Type.INT) {
            writer.print("int");
        } else if (type == Type.NUMBR) {
            writer.print("double");
        } else if (type == Type.YARN) {
            writer.print("wchar_t*");
        } else if (type == Type.B00L) {
            writer.print("int");
        } else if (type == Type.KAR) {
            writer.print("wchar_t");
        } else if (type instanceof BukkitType) {
            writer.print("struct _Bukkit" + type.getId() + "*");
        } else if (type instanceof ArrayType) {
            translateType(ArrayType.class.cast(type).getBaseType(), writer);
            writer.print("*");
        } else {
            // Should have any real types here, but just in case...
            writer.print("int");
        }
    }

    /**
     * Writes the C string representation of a Roflkode variable.
     */
    private void translateVariableName(Variable variable, PrintWriter writer) {
        writer.print("_v" + variable.getId());
    }

    /**
     * Writes the C string representation of a Roflkode bukkit property.
     */
    private void translatePropertyName(Property property, PrintWriter writer) {
        writer.print("_p" + property.getId());
    }

    /**
     * Returns a string representation, in the language C, of the given Roflkode script.
     * Precondition: The script has already been semantically analyzed and is error-free.
     */
    public void translateScript(Script script, PrintWriter writer) {

        writer.println("#include <stdio.h>");
        writer.println("#include <stdlib.h>");
        writer.println("#include <math.h>");

        // First fetch all the bukkit types.  Render declarations then definitions.
        List<BukkitType> bukkitTypes = fetchAllBukkitTypes(script);

        for (BukkitType b : bukkitTypes) {
            writer.println("struct _Bukkit" + b.getId() + ";");
        }

        for (BukkitType b : bukkitTypes) {
            writer.println("struct _Bukkit" + b.getId() + " {");
            for (Property p: b.getProperties()) {
                writer.print("    ");
                translateType(p.getType(), writer);
                writer.print(" ");
                translatePropertyName(p, writer);
                writer.println(";");
            }
            writer.println("};");
        }

        // Next fetch all the functions.  Render signatures then bodies.  Note that externs will
        // of course have only signatures.

        // TODO

        // Finally put the statements of the top-level statements in the main() function.
        writer.println("int main() {");
        for (Statement statement: script.getStatements()) {
            translateStatement(statement, writer, "    ");
        }
        writer.println("    return 0;");
        writer.println("}");
    }

    private List<BukkitType> fetchAllBukkitTypes(Script script) {
        final List<BukkitType> types = new ArrayList<BukkitType>();
        script.traverse(new Visitor() {
            public void visit(Entity e) {
                if (e.getClass() == BukkitType.class) {
                    types.add(BukkitType.class.cast(e));
                }
            }
        });
        return types;
    }

    private void translateStatement(Statement s, PrintWriter writer, String indent) {

        if (s instanceof BukkitType || s instanceof Function) {
            // All bukkit types and functions go at the top level, so don't write them here
            return;
        }

        writer.print(indent);

        if (s instanceof Variable) {
            translateVariableDeclaration(Variable.class.cast(s), writer);
        } else if (s instanceof UpzorzStatement) {
            writer.append("++");
            translateVariableExpression(UpzorzStatement.class.cast(s).getTarget(), writer);
        } else {
            writer.print("TODO");
        }
        writer.println();
    }

    private void translateVariableDeclaration(Variable v, PrintWriter writer) {
        if (v.isConstant()) {
            writer.print("const ");
        }
        translateType(v.getType(), writer);
        writer.print(" ");
        translateVariableName(v, writer);
        if (v.getInitializer() != null) {
            writer.print(" = ");
            translateExpression(v.getInitializer(), writer);
        }
        writer.print(";");
    }

    private void translateExpression(Expression e, PrintWriter writer) {
        if (e instanceof IntegerLiteral) {
            writer.print(IntegerLiteral.class.cast(e).getValue());
        } else if (e instanceof NoobLiteral) {
            writer.print("0");
        } else if (e instanceof VariableExpression) {
            translateVariableExpression(VariableExpression.class.cast(e), writer);
        } else {
            writer.print("TODO");
        }
    }

    private void translateVariableExpression(VariableExpression e, PrintWriter writer) {
        if (e instanceof SimpleVariableExpression) {
            SimpleVariableExpression v = SimpleVariableExpression.class.cast(e);
            translateVariableName(v.getReferent(), writer);
        } else if (e instanceof PropertyVariableExpression) {
            PropertyVariableExpression v = PropertyVariableExpression.class.cast(e);
            translateVariableExpression(v.getBukkit(), writer);
            writer.print(".");
            translatePropertyName(v.getProperty(), writer);
        } else if (e instanceof IndexVariableExpression) {
            IndexVariableExpression v = IndexVariableExpression.class.cast(e);
            translateVariableExpression(v.getArray(), writer);
            writer.print("[");
            translateExpression(v.getIndex(), writer);
            writer.print("]");
        } else {
            writer.print("TODO");
        }
    }
}
