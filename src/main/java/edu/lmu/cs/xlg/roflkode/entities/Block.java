package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode block.  A block is a sequence of statements that defines a scope.
 */
public class Block extends Entity {

    private List<Statement> statements;

    /**
     * Creates a block.
     */
    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    /**
     * Returns the statement list comprising the block.
     */
    public List<Statement> getStatements() {
        return statements;
    }
}
