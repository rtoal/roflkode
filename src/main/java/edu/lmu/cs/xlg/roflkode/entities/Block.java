package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode block.  A block is a sequence of statements that defines a scope.
 */
public class Block extends Entity {

    List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
