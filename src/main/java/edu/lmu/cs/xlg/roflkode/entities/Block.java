package edu.lmu.cs.xlg.roflkode.entities;

import java.util.ArrayList;
import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode block.  A block is a sequence of statements that defines a scope.
 */
public class Block extends Entity {

    private List<Statement> statements;
    private SymbolTable table = null;

    /**
     * Creates a block.
     */
    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    /**
     * Returns the statement list immediately within the block, in the order that they appear.
     */
    public List<Statement> getStatements() {
        return statements;
    }


    /**
     * Returns the block's symbol table.
     */
    public SymbolTable getTable() {
        return table;
    }

    /**
     * Returns the list of all the types declared immediately within this block, in the order they
     * are declared.
     */
    public List<Type> getTypes() {
        List<Type> result = new ArrayList<Type>();
        for (Statement s: statements) {
            if (s instanceof Type) {
                result.add((Type)s);
            }
        }
        return result;
    }

    /**
     * Returns the list of all the functions declared immediately within this block, in the order
     * they are declared.
     */
    public List<Function> getFunctions() {
        List<Function> result = new ArrayList<Function>();
        for (Statement s: statements) {
            if (s instanceof Function) {
                result.add((Function)s);
            }
        }
        return result;
    }

    /**
     * Creates the symbol table for this block.
     */
    public void createTable(SymbolTable parent) {
        if (parent != null) {
            // MAYBE: Consider throwing an error here.
        }
        table = new SymbolTable(parent);
    }

    /**
     * Performs semantic analysis on this block.
     */
    public void analyze(Log log, SymbolTable outer, Function owner, boolean inLoop) {

        List<Type> types = getTypes();
        List<Function> functions = getFunctions();

        // Create the table if it hasn't already been created.  For blocks that are bodies of
        // functions or iteration statements, the analyze() method of the function or statement
        // will have created this table already, since it is the table in which the parameters or
        // or iteration statement index belong.  For blocks that are entire scripts, the table will
        // have already been created, too.  All other blocks will need their tables created here.
        if (table == null) {
            table = new SymbolTable(outer);
        }

        // Bukkit types should go into the table first.  They can be used for everything in this
        // scope: function return types, parameter types, variable types, field types, etc.  Note
        // that they go into the symbol table without being analyzed, because when analyzing them
        // we have to check the types of their fields, and these fields may refer to other bukkit
        // types declared in this block.
        for (Type type: types) {
            table.insert(type, log);
        }

        // Pre-analyze bukkit types so the fields are available. This has to be done AFTER all the
        // struct types have been added to the symbol table, but BEFORE any variables are handled,
        // since the variables may refer to bukkit properties in their initializing expressions.
        for (Type type: types) {
            type.analyze(log, table, owner, inLoop);
        }

        // Insert the functions into the table, but analyze ONLY the parameters and return types.
        // We can't analyze the function bodies until all the functions have been put into the
        // table (with analyzed parameters) because within any function body there can be a call
        // to any other function, and we have to be able to analyze the call.  Notice also that the
        // functions are going in before any variables are being looked at since variables can call
        // any function in their initializing expressions.
        for (Function function: functions) {
            function.analyzeSignature(log, table, owner, inLoop);
            table.insert(function, log);
        }

        // Now just go through all the items in order and analyze everything, inserting variables as
        // you go.  The variables have to be inserted during this final pass, since they are only in
        // scope from their point of declaration onward. (In other words, if we tried to first
        // insert all the variables and then analyze them later, that would have been wrong.)
        for (Statement s: statements) {
            if (s instanceof Variable) {
                table.insert((Variable)s, log);
            }
            if (s instanceof Type) {
                // Don't analyze types again
                continue;
            }
            s.analyze(log, table, owner, inLoop);
        }
    }
}
