package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode function.  Functions whose body is null are "external" functions, whose signature
 * is defined in Roflkode but whose body is linked in.
 */
public class Function extends Entity {

    private String name;
    private String returnTypeName;
    private List<Variable> parameters;
    private Block body;

    /**
     * Creates a normal function object.
     */
    public Function(String returnTypeName, String name, List<Variable> parameters, Block body) {
        this.name = name;
        this.returnTypeName = returnTypeName;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
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
}
