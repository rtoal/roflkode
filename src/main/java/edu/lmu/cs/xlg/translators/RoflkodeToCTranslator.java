package edu.lmu.cs.xlg.translators;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.lmu.cs.xlg.roflkode.entities.AgainStatement;
import edu.lmu.cs.xlg.roflkode.entities.ArrayExpression;
import edu.lmu.cs.xlg.roflkode.entities.ArrayType;
import edu.lmu.cs.xlg.roflkode.entities.AssignmentStatement;
import edu.lmu.cs.xlg.roflkode.entities.BinaryExpression;
import edu.lmu.cs.xlg.roflkode.entities.Block;
import edu.lmu.cs.xlg.roflkode.entities.BooleanLiteral;
import edu.lmu.cs.xlg.roflkode.entities.BrbStatement;
import edu.lmu.cs.xlg.roflkode.entities.BukkitExpression;
import edu.lmu.cs.xlg.roflkode.entities.BukkitType;
import edu.lmu.cs.xlg.roflkode.entities.BukkitType.Property;
import edu.lmu.cs.xlg.roflkode.entities.CallStatement;
import edu.lmu.cs.xlg.roflkode.entities.ConditionalStatement;
import edu.lmu.cs.xlg.roflkode.entities.DiafStatement;
import edu.lmu.cs.xlg.roflkode.entities.Entity;
import edu.lmu.cs.xlg.roflkode.entities.Entity.Visitor;
import edu.lmu.cs.xlg.roflkode.entities.Expression;
import edu.lmu.cs.xlg.roflkode.entities.FacepalmStatement;
import edu.lmu.cs.xlg.roflkode.entities.Function;
import edu.lmu.cs.xlg.roflkode.entities.GimmehStatement;
import edu.lmu.cs.xlg.roflkode.entities.GtfoStatement;
import edu.lmu.cs.xlg.roflkode.entities.HerezStatement;
import edu.lmu.cs.xlg.roflkode.entities.IndexVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.IntegerLiteral;
import edu.lmu.cs.xlg.roflkode.entities.KarLiteral;
import edu.lmu.cs.xlg.roflkode.entities.Literal;
import edu.lmu.cs.xlg.roflkode.entities.LoopStatement;
import edu.lmu.cs.xlg.roflkode.entities.ModifiedSimpleStatement;
import edu.lmu.cs.xlg.roflkode.entities.ModifiedSimpleStatement.Modifier;
import edu.lmu.cs.xlg.roflkode.entities.NerfzorzStatement;
import edu.lmu.cs.xlg.roflkode.entities.NoobLiteral;
import edu.lmu.cs.xlg.roflkode.entities.NumberLiteral;
import edu.lmu.cs.xlg.roflkode.entities.PropertyVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.Script;
import edu.lmu.cs.xlg.roflkode.entities.SimpleVariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.Statement;
import edu.lmu.cs.xlg.roflkode.entities.StringLiteral;
import edu.lmu.cs.xlg.roflkode.entities.SwitchStatement;
import edu.lmu.cs.xlg.roflkode.entities.TryStatement;
import edu.lmu.cs.xlg.roflkode.entities.Type;
import edu.lmu.cs.xlg.roflkode.entities.UnaryExpression;
import edu.lmu.cs.xlg.roflkode.entities.UpzorzStatement;
import edu.lmu.cs.xlg.roflkode.entities.Variable;
import edu.lmu.cs.xlg.roflkode.entities.VariableExpression;
import edu.lmu.cs.xlg.roflkode.entities.YoStatement;

/**
 * A translator that produces a C program for a given Roflkode script.  Translator objects are
 * <strong>NOT THREAD SAFE</strong>, but that is not a big deal because this class has a private
 * constructor and a new translator is created every time you call the static translate method.
 */
public class RoflkodeToCTranslator {

    @SuppressWarnings("serial")
    private static final Map<String, String> C_BINARY_OPERATORS = new HashMap<String, String>() {{
        put("ORELSE", "||");
        put("ANALSO", "&&");
        put("BITOR", "|");
        put("BITAND", "&");
        put("BITXOR", "^");
    }};

    /**
     * Translates the given script, writing the target C program to the given writer.
     */
    public static void translate(Script script, PrintWriter writer) {
        new RoflkodeToCTranslator(writer).translateScript(script);
    }

    private PrintWriter writer;
    private List<Function> functions = new ArrayList<Function>();
    private List<BukkitType> bukkitTypes = new ArrayList<BukkitType>();
    private Set<Variable> nonLocallyAccessedVariables = new HashSet<Variable>();

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

        // Get all the bukkit types, functions, and non-locally accessed variables.
        preprocess(script);

        for (Variable v :  nonLocallyAccessedVariables) {
            writer.println("int __offset_" + v.getId() + ";");
        }

        // Emit type declarations then definitions, to handle mutual recursion.
        for (BukkitType b : bukkitTypes) {
            translateStructTypeDeclaration(b);
        }

        for (BukkitType b : bukkitTypes) {
            translateStructTypeDefinition(b);
        }

        // Emit function sigs THEN whole bodies, to handle mutual recursion.
        for (Function f: functions) {
            translateFunctionSignature(f);
            writer.println(";");
        }

        for (Function f: functions) {
            translateWholeFunction(f);
        }

        // Finally put the statements of the top-level statements in the main() function.
        writer.println("int main() {");
        translateBlock(script, "    ");
        writer.println("    return 0;");
        writer.println("}");
    }


    private void translateStructTypeDeclaration(BukkitType type) {
        writer.println("struct _Bukkit" + type.getId() + ";");
    }

    private void translateStructTypeDefinition(BukkitType type) {
        writer.println("struct _Bukkit" + type.getId() + " {");
        for (Property p: type.getProperties()) {
            writer.print("    ");
            translateType(p.getType());
            writer.print(" ");
            translatePropertyName(p);
            writer.println(";");
        }
        writer.println("};");
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
            // Ww should not have any real types here, but just in case...
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
     * Writes the C string representation of a Roflkode function name.
     */
    private void translateFunctionName(Function function) {
        if (function.getBody() != null) {
            writer.print("_f" + function.getId());
        } else {
            writer.print("__Roflkode__" + function.getName());
        }
    }

    /**
     * Writes the C label for the top of the given loop.
     */
    private void translateLoopTop(LoopStatement s) {
        writer.print("top" + s.getId());
    }

    /**
     * Writes the C label for the top of the given loop.
     */
    private void translateLoopBottom(LoopStatement s) {
        writer.print("bottom" + s.getId());
    }

    /**
     * Writes the function signature, in C, only.
     */
    private void translateFunctionSignature(Function function) {
        if (function.getReturnType() == null) {
            writer.print("void");
        } else {
            translateType(function.getReturnType());
        }
        writer.print(" ");
        translateFunctionName(function);
        translateParameters(function.getParameters());
    }

    /**
     * Writes the given function in C, with the signature and the body, if applicable.  If there
     * is no body, the function is external and nothing at all is written.
     */
    private void translateWholeFunction(Function function) {
        if (function.getBody() == null) {
            // Do nothing, the signature was already done and there is not body
            return;
        }
        translateFunctionSignature(function);
        writer.println(" {");
        translateBlock(function.getBody(), "    ");
        writer.println("}");
    }

    /**
     * Writes the given parameter list in C, with parentheses.
     */
    private void translateParameters(List<Variable> parameters) {
        writer.print("(");
        boolean first = true;
        for (Variable v: parameters) {
            if (!first) {
                writer.print(", ");
            }
            translateType(v.getType());
            writer.print(" ");
            translateVariableName(v);
            first = false;
        }
        writer.print(")");
    }

    private void translateBlock(Block b, String indent) {
        for (Statement s: b.getStatements()) {
            translateStatement(s, indent);
        }
    }

    private void translateStatement(Statement s, String indent) {
        if (s instanceof Variable) {
            translateVariableDeclaration(Variable.class.cast(s), indent);
        } else if (s instanceof BukkitType) {
            // All types already defined at the top-level of the script
            return;
        } else if (s instanceof Function) {
            // All functions already defined at the top-level of the script
            return;
        } else if (s instanceof YoStatement) {
            translateYoStatement(YoStatement.class.cast(s), indent);
        } else if (s instanceof FacepalmStatement) {
            translateFacepalmStatement(FacepalmStatement.class.cast(s), indent);
        } else if (s instanceof UpzorzStatement) {
            translateUpzorzStatement(UpzorzStatement.class.cast(s), indent);
        } else if (s instanceof NerfzorzStatement) {
            translateNerfzorzStatement(NerfzorzStatement.class.cast(s), indent);
        } else if (s instanceof AssignmentStatement) {
            translateAssignmentStatement(AssignmentStatement.class.cast(s), indent);
        } else if (s instanceof GtfoStatement) {
            translateGtfoStatement(GtfoStatement.class.cast(s), indent);
        } else if (s instanceof AgainStatement) {
            translateAgainStatement(AgainStatement.class.cast(s), indent);
        } else if (s instanceof HerezStatement) {
            translateHerezStatement(HerezStatement.class.cast(s), indent);
        } else if (s instanceof DiafStatement) {
            translateDiafStatement(DiafStatement.class.cast(s), indent);
        } else if (s instanceof GimmehStatement) {
            translateGimmehStatement(GimmehStatement.class.cast(s), indent);
        } else if (s instanceof HerezStatement) {
            translateHerezStatement(HerezStatement.class.cast(s), indent);
        } else if (s instanceof BrbStatement) {
            translateBrbStatement(BrbStatement.class.cast(s), indent);
        } else if (s instanceof CallStatement) {
            translateCallStatement(CallStatement.class.cast(s), indent);
        } else if (s instanceof ModifiedSimpleStatement) {
            translateModifiedStatement(ModifiedSimpleStatement.class.cast(s), indent);
        } else if (s instanceof ConditionalStatement) {
            translateConditionalStatement(ConditionalStatement.class.cast(s), indent);
        } else if (s instanceof LoopStatement) {
            translateLoopStatement(LoopStatement.class.cast(s), indent);
        } else if (s instanceof SwitchStatement) {
            translateSwitchStatement(SwitchStatement.class.cast(s), indent);
        } else if (s instanceof TryStatement) {
            translateTryStatement(TryStatement.class.cast(s), indent);
        } else {
            throw new RuntimeException("FATAL ERROR: TRANSLATOR DOESN'T KNOW ABOUT " +
                    s.getClass().getName());
        }
    }

    private void translateVariableDeclaration(Variable v, String indent) {
        writer.print(indent);
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
        writer.println(";");
    }

    private void translateYoStatement(YoStatement s, String indent) {
        for (Expression e: s.getExpressions()) {
            writer.print(indent);
            if (e.getType() == Type.B00L) {
                writer.print("printf(");
                translateExpression(e);
                writer.println(" ? \"WIN\" : \"FAIL\");");
            } else if (e.getType() == Type.KAR) {
                writer.println("TODO_CODE_TO_PRINT_KAR");
            } else if (e.getType() == Type.INT) {
                writer.print("printf(\"%d\", ");
                translateExpression(e);
                writer.println(");");
            } else if (e.getType() == Type.NUMBR) {
                writer.print("printf(\"%g\", ");
                translateExpression(e);
                writer.println(");");
            } else if (e.getType() == Type.YARN) {
                writer.print("printf(\"%s\", ");
                translateExpression(e);
                writer.println(");");
            } else if (e.getType() instanceof ArrayType) {
                writer.println("TODO_CODE_TO_PRINT_ARRAY");
            } else if (e.getType() instanceof BukkitType) {
                writer.println("TODO_CODE_TO_PRINT_BUKKIT");
            } else {
                throw new RuntimeException("Unknown type made it to code generator");
            }
        }
    }

    private void translateFacepalmStatement(FacepalmStatement s, String indent) {
        writer.println(indent + "TODO_Facepalm_Statement");
    }

    private void translateUpzorzStatement(UpzorzStatement s, String indent) {
        writer.print(indent + "++");
        translateVariableExpression(s.getTarget());
        writer.println(";");
    }

    private void translateNerfzorzStatement(NerfzorzStatement s, String indent) {
        writer.print(indent + "--");
        translateVariableExpression(s.getTarget());
        writer.println(";");
    }

    private void translateAssignmentStatement(AssignmentStatement s, String indent) {
        writer.print(indent);
        translateVariableExpression(s.getLeft());
        writer.print(" = ");
        translateExpression(s.getRight());
        writer.println(";");
    }

    private void translateGtfoStatement(GtfoStatement s, String indent) {
        if (s.getTarget() instanceof LoopStatement) {
            writer.print(indent + "goto ");
            translateLoopBottom(LoopStatement.class.cast(s.getTarget()));
            writer.println(";");
        } else {
            writer.println(indent + "return;");
        }
    }

    private void translateAgainStatement(AgainStatement s, String indent) {
        writer.print(indent + "goto ");
        translateLoopTop(s.getLoop());
        writer.println(";");
    }

    private void translateHerezStatement(HerezStatement s, String indent) {
        writer.print(indent + "return ");
        translateExpression(s.getExpression());
        writer.println(";");
    }

    private void translateDiafStatement(DiafStatement s, String indent) {
        writer.print(indent + "__Roflkode__Diaf(");
        translateExpression(s.getExpression());
        writer.println(");");
    }

    private void translateGimmehStatement(GimmehStatement s, String indent) {
        writer.print(indent + "__Roflkode__Gimmeh");
        writer.print(s.getTarget().getType().getName().toLowerCase());
        writer.print("(&");
        translateExpression(s.getTarget());
        writer.println(");");
    }

    private void translateBrbStatement(BrbStatement s, String indent) {
        writer.print(indent + "__Roflkode__Brb(");
        translateExpression(s.getExpression());
        writer.println(");");
    }

    private void translateCallStatement(CallStatement s, String indent) {
        writer.println(indent + "TODO_Call_Statement");
    }

    private void translateModifiedStatement(ModifiedSimpleStatement s, String indent) {
        boolean negate = false;
        String keyword = "if";
        if (s.getModifier() == Modifier.CEPT) {
            negate = true;
        } else if (s.getModifier() == Modifier.WHIEL) {
            keyword = "while";
        } else if (s.getModifier() == Modifier.TIL) {
            keyword = "while";
            negate = true;
        }
        writer.print(indent + keyword + " (");
        if (negate) {
            writer.print("!(");
        }
        translateExpression(s.getExpression());
        if (negate) {
            writer.print(")");
        }
        writer.println(") {");
        translateStatement(s.getStatement(), indent + "   ");
        writer.println(indent + "}");
    }

    private void translateConditionalStatement(ConditionalStatement s, String indent) {
        writer.println(indent + "TODO_CONDITIONAL_STATEMENT");
    }

    private void translateLoopStatement(LoopStatement s, String indent) {
        writer.println(indent);
        translateLoopTop(s);
        writer.println(":");
        if ("WHIEL".equals(s.getLoopType())) {
            writer.print(indent + "if (!(");
            translateExpression(s.getCondition());
            writer.print(")) goto ");
            translateLoopBottom(s);
            writer.println(";");
            translateBlock(s.getBody(), indent);
            writer.print(indent + "goto ");
            translateLoopTop(s);
            writer.println(";");
        } else if ("TIL".equals(s.getLoopType())) {

        } else if (s.getCollection() != null) {
            writer.println("TODO - THRU COLLECTION");
        } else if (s.getStart() != null) {
            writer.println("TO DO - FROM/TO RANGE");
        } else {
            // Plain ol' loop
            translateBlock(s.getBody(), indent);
            writer.print(indent + "goto ");
            translateLoopTop(s);
            writer.println(";");
        }
        writer.println(indent);
        translateLoopBottom(s);
        writer.println(": ;");
    }

    private void translateSwitchStatement(SwitchStatement s, String indent) {
        writer.println(indent + "TODO_SWITCH_STATEMENT");
    }

    private void translateTryStatement(TryStatement s, String indent) {
        writer.println(indent + "TODO_TRY_STATEMENT");
    }

    private void translateExpression(Expression e) {
        if (e instanceof Literal) {
            translateLiteral(Literal.class.cast(e));
        } else if (e instanceof VariableExpression) {
            translateVariableExpression(VariableExpression.class.cast(e));
        } else if (e instanceof UnaryExpression) {
            translateUnaryExpression(UnaryExpression.class.cast(e));
        } else if (e instanceof BinaryExpression) {
            translateBinaryExpression(BinaryExpression.class.cast(e));
        } else if (e instanceof ArrayExpression) {
            translateArrayExpression(ArrayExpression.class.cast(e));
        } else /* BukkitExpression */ {
            translateBukkitExpression(BukkitExpression.class.cast(e));
        }
    }

    private void translateLiteral(Literal e) {
        if (e instanceof NoobLiteral) {
            writer.print("0");
        } else if (e instanceof BooleanLiteral) {
            writer.print(e == BooleanLiteral.WIN ? "1" : "0");
        } else if (e instanceof IntegerLiteral) {
            writer.print(IntegerLiteral.class.cast(e).getValue());
        } else if (e instanceof NumberLiteral) {
            writer.print(NumberLiteral.class.cast(e).getValue());
        } else if (e instanceof KarLiteral) {
            writer.print("L'" + character(KarLiteral.class.cast(e).getCodepoint()) + "'");
        } else /* StringLiteral */ {
            writer.print("L\"");
            for (Integer codepoint: StringLiteral.class.cast(e).getCodepoints()) {
                writer.print(character(codepoint));
            }
            writer.print("\"");
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
            writer.print("TODO_FUNCTION_CALL");
        }
    }

    private void translateUnaryExpression(UnaryExpression e) {
        writer.print("TODO_UNARY_EXPRESSION");
    }

    private void translateBinaryExpression(BinaryExpression e) {
        writer.print("(");
        if ("~~".equals(e.getOp())) {
            writer.print("TODO_CONCAT_EXPRESSION");
        } else if ("\\".equals(e.getOp())) {
            translateExpression(e.getLeft());
            writer.print("%");
            translateExpression(e.getRight());
            writer.print("!=0");
        } else {
            translateExpression(e.getLeft());
            String op = C_BINARY_OPERATORS.get(e.getOp());
            writer.print(op == null ? e.getOp() : op);
            translateExpression(e.getRight());
        }
        writer.print(")");
    }

    private void translateArrayExpression(ArrayExpression e) {
        writer.print("TODO_ARRAY_EXPRESSION");
    }

    private void translateBukkitExpression(BukkitExpression e) {
        writer.print("TODO_BUKKIT_EXPRESSION");
    }

    private void preprocess(Script script) {
        script.traverse(new Visitor() {
            public void visit(Entity e) {
                if (e.getClass() == BukkitType.class) {
                    bukkitTypes.add(BukkitType.class.cast(e));
                } else if (e.getClass() == Function.class) {
                    functions.add(Function.class.cast(e));
                } else if (e.getClass() == SimpleVariableExpression.class) {
                    SimpleVariableExpression v = SimpleVariableExpression.class.cast(e);
                    if (v.isNonLocal()) {
                        nonLocallyAccessedVariables.add(v.getReferent());
                    }
                }
            }
        });
    }

    /**
     * Returns a representation of a character for use in a C character or string literal.
     * Printable ASCII characters are returned as is; all other characters are returned in the
     * most suitable form from \xhh, \ uhhhh, or \Uhhhhhhhh.
     */
    String character(int codepoint) {
        if (0x20 <= codepoint && codepoint <= 0x7f) {
            return Character.toString((char)codepoint);
        } else if (codepoint <= 0xff) {
            return String.format("\\x%02x", codepoint);
        } else if (codepoint <= 0xffff) {
            return String.format("\\u%04x", codepoint);
        } else {
            return String.format("\\U%08x", codepoint);
        }
    }
}
