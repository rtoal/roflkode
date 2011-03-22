package edu.lmu.cs.xlg.roflkode.entities;

/**
 * A Roflkode declaration statement.
 */
public class DeclarationStatement extends Entity {

    private Entity entity;

    public DeclarationStatement(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
