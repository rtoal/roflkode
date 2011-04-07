package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode function.  Functions whose body is null are "external" functions, whose signature
 * is defined in Roflkode but whose body is linked in.
 */
public class Function extends Declaration {

    private String returnTypeName;
    private List<Variable> parameters;
    private Block body;
    private Type returnType;

    /**
     * Creates a normal function object.
     */
    public Function(String returnTypeName, String name, List<Variable> parameters, Block body) {
        super(name);
        this.returnTypeName = returnTypeName;
        this.parameters = parameters;
        this.body = body;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public Block getBody() {
        return body;
    }

    public Type getReturnType() {
        return returnType;
    }

    /**
     * Performs semantic analysis on the function's signature and return type, but not the body.
     */
    public void analyzeSignature(Log log, SymbolTable table, Function owner, boolean inLoop) {
        returnType = returnTypeName == null ? null : table.lookupType(returnTypeName, log);
        body.createTable(table);
        for (Variable parameter: parameters) {
            body.getTable().insert(parameter, log);
            parameter.analyze(log, body.getTable(), owner, inLoop);
        }
    }

    /**
     * Performs semantics analysis on the body.  This is done after the signature has been analyzed,
     * so the body's symbol table has already been constructed and the parameters have already been
     * loaded.
     */
    public void analyze(Log log, SymbolTable table, Function owner, boolean inLoop) {
        body.analyze(log, table, this, false);
    }
}
