package edu.lmu.cs.xlg.translators;

import java.util.ArrayList;
import java.util.List;

import edu.lmu.cs.xlg.roflkode.entities.AssignmentStatement;
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

    private String typename(Type type) {
        if (type == Type.INT) {
            return "int";
        } else if (type == Type.NUMBR) {
            return "double";
        } else if (type == Type.YARN) {
            return "char *";
        } else if (type == Type.B00L) {
            return "int";
        } else if (type == Type.KAR) {
            return "wchar_t";
        } else if (type instanceof BukkitType) {
            return "struct _Bukkit" + type.getId();
        } else {
            return "TODO";
        }
    }

    private String variableName(Variable variable) {
        return "_v" + variable.getId();
    }

    private String propertyName(Property property) {
        return "_p" + property.getId();
    }

    /**
     * Returns a string representation, in the language C, of the given Roflkode script.
     * Precondition: The script has already been semantically analyzed and is error-free.
     */
    public String toC(Script script) {

        StringBuilder builder = new StringBuilder();

        builder.append("#include <stdio.h>\n");
        builder.append("#include <stdlib.h>\n");
        builder.append("#include <math.h>\n");

        // First fetch all the bukkit types.  Render declarations then definitions.
        List<BukkitType> bukkitTypes = fetchAllBukkitTypes(script);

        for (BukkitType b : bukkitTypes) {
            builder.append(typename(b)).append(";\n");
        }
        for (BukkitType b : bukkitTypes) {
            builder.append(typename(b)).append("{\n");
            for (Property p: b.getProperties()) {
                builder.append("    ").append(typename(p.getType())).append(" ")
                        .append(propertyName(p)).append(";\n");
            }
            builder.append("};\n");
        }

        // Next fetch all the functions.  Render signatures then bodies.  Note that externs will
        // of course have only signatures.

        // TODO

        // Finally put the statements of the top-level statements in the main() function.
        writeMainFunction(script, builder);

        return builder.toString();
    }

    private List<BukkitType> fetchAllBukkitTypes(Script s) {
        final List<BukkitType> types = new ArrayList<BukkitType>();
        s.traverse(new Visitor() {
            public void visit(Entity e) {
                if (e.getClass() == BukkitType.class) {
                    types.add(BukkitType.class.cast(e));
                }
            }
        });
        return types;
    }

    private void writeMainFunction(Script script, StringBuilder builder) {
        builder.append("int main() {\n");
        for (Statement s: script.getStatements()) {
            writeStatement(s, builder, "    ");
        }
        builder.append("}\n");
    }

    private void writeStatement(Statement s, StringBuilder builder, String indent) {
        if (s instanceof BukkitType || s instanceof Function) {
            // All bukkit types and functions go at the top level, so don't write them here
            return;
        }

        builder.append(indent);
        if (s instanceof Variable) {
            writeVariable(Variable.class.cast(s), builder);
        } else if (s instanceof UpzorzStatement) {
            builder.append("++");
            writeVariableExpression(UpzorzStatement.class.cast(s).getTarget(), builder);
        } else {
            builder.append("TODO");
        }
        builder.append("\n");
    }

    private void writeVariable(Variable v, StringBuilder builder) {
        if (v.isConstant()) {
            builder.append("const ");
        }
        builder.append(typename(v.getType())).append(" ").append(variableName(v));
        if (v.getInitializer() != null) {
            builder.append(" = ");
            writeExpression(v.getInitializer(), builder);
        }
        builder.append(";");
    }

    private void writeExpression(Expression e, StringBuilder builder) {
        if (e instanceof IntegerLiteral) {
            builder.append(IntegerLiteral.class.cast(e).getValue());
        } else if (e instanceof NoobLiteral) {
            builder.append("0");
        } else if (e instanceof VariableExpression) {
            writeVariableExpression(VariableExpression.class.cast(e), builder);
        } else {
            builder.append("TODO");
        }
    }

    private void writeVariableExpression(VariableExpression e, StringBuilder builder) {
        if (e instanceof SimpleVariableExpression) {
            SimpleVariableExpression v = SimpleVariableExpression.class.cast(e);
            builder.append(variableName(v.getReferent()));
        } else if (e instanceof PropertyVariableExpression) {
            PropertyVariableExpression v = PropertyVariableExpression.class.cast(e);
            writeVariableExpression(v.getBukkit(), builder);
            builder.append(".");
            builder.append(propertyName(v.getProperty()));
        } else if (e instanceof IndexVariableExpression) {
            IndexVariableExpression v = IndexVariableExpression.class.cast(e);
            writeVariableExpression(v.getArray(), builder);
            builder.append("[");
            writeExpression(v.getIndex(), builder);
            builder.append("]");
        } else {
            builder.append("TODO");
        }
    }
}
