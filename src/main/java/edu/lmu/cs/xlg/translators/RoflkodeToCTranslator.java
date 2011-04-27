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

/**
 * A translator that produces a C program for a given Roflkode script.  Translator objects are
 * <strong>NOT THREAD SAFE</strong>, but that is not a big deal because this class has a private
 * constructor and a new translator is created every time you call the static translate method.
 */
public class RoflkodeToCTranslator {

    /**
     * Translates the given script, writing the target C program to the given writer.
     */
    public static void translate(Script script, PrintWriter writer) {
        new RoflkodeToCTranslator(writer).translateScript(script);
    }

    private PrintWriter writer;

    private RoflkodeToCTranslator(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * Writes a C representation of the given Roflkode script to the translator's writer.
     * Precondition: The script has already been semantically analyzed and is error-free.
     */
    private void translateScript(Script script) {

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
                translateType(p.getType());
                writer.print(" ");
                translatePropertyName(p);
                writer.println(";");
            }
            writer.println("};");
        }

        // Next fetch all the functions.  Render signatures then bodies.  Note that externs will
        // of course have only signatures.
        List<Function> functions = fetchAllFunctions(script);

        for (Function f: functions) {
            writer.print("TODO - function ");
            translateFunctionName(f);
            writer.println(" goes here");
        }
        // TODO

        // Finally put the statements of the top-level statements in the main() function.
        writer.println("int main() {");
        for (Statement statement: script.getStatements()) {
            translateStatement(statement, "    ");
        }
        writer.println("    return 0;");
        writer.println("}");
    }


    /**
     * Writes the C string representation of a Roflkode type.  Notice that YARNs, Bukkits, and
     * arrays are all reference types in Roflkode, so these are all represented as pointer types
     * in C.
     */
    private void translateType(Type type) {
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
            translateType(ArrayType.class.cast(type).getBaseType());
            writer.print("*");
        } else {
            // Should have any real types here, but just in case...
            writer.print("int");
        }
    }

    /**
     * Writes the C string representation of a Roflkode variable.
     */
    private void translateVariableName(Variable variable) {
        writer.print("_v" + variable.getId());
    }

    /**
     * Writes the C string representation of a Roflkode bukkit property.
     */
    private void translatePropertyName(Property property) {
        writer.print("_p" + property.getId());
    }

    /**
     * Writes the C string representation of a Roflkode function.
     */
    private void translateFunctionName(Function function) {
        writer.print("_f" + function.getId());
    }

    private void translateStatement(Statement s, String indent) {

        if (s instanceof BukkitType || s instanceof Function) {
            // All bukkit types and functions go at the top level, so don't write them here
            return;
        }

        writer.print(indent);

        if (s instanceof Variable) {
            translateVariableDeclaration(Variable.class.cast(s));
        } else if (s instanceof UpzorzStatement) {
            writer.append("++");
            translateVariableExpression(UpzorzStatement.class.cast(s).getTarget());
        } else {
            writer.print("TODO");
        }
        writer.println();
    }

    private void translateVariableDeclaration(Variable v) {
        if (v.isConstant()) {
            writer.print("const ");
        }
        translateType(v.getType());
        writer.print(" ");
        translateVariableName(v);
        if (v.getInitializer() != null) {
            writer.print(" = ");
            translateExpression(v.getInitializer());
        }
        writer.print(";");
    }

    private void translateExpression(Expression e) {
        if (e instanceof IntegerLiteral) {
            writer.print(IntegerLiteral.class.cast(e).getValue());
        } else if (e instanceof NoobLiteral) {
            writer.print("0");
        } else if (e instanceof VariableExpression) {
            translateVariableExpression(VariableExpression.class.cast(e));
        } else {
            writer.print("TODO");
        }
    }

    private void translateVariableExpression(VariableExpression e) {
        if (e instanceof SimpleVariableExpression) {
            SimpleVariableExpression v = SimpleVariableExpression.class.cast(e);
            translateVariableName(v.getReferent());
        } else if (e instanceof PropertyVariableExpression) {
            PropertyVariableExpression v = PropertyVariableExpression.class.cast(e);
            translateVariableExpression(v.getBukkit());
            writer.print(".");
            translatePropertyName(v.getProperty());
        } else if (e instanceof IndexVariableExpression) {
            IndexVariableExpression v = IndexVariableExpression.class.cast(e);
            translateVariableExpression(v.getArray());
            writer.print("[");
            translateExpression(v.getIndex());
            writer.print("]");
        } else {
            writer.print("TODO");
        }
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

    private List<Function> fetchAllFunctions(Script script) {
        final List<Function> functions = new ArrayList<Function>();
        script.traverse(new Visitor() {
            public void visit(Entity e) {
                if (e.getClass() == Function.class) {
                    functions.add(Function.class.cast(e));
                }
            }
        });
        return functions;
    }
}
