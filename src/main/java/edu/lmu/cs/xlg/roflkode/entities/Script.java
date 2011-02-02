package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode script.
 */
public class Script {

    List<Statement> statements;

    public Script(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
