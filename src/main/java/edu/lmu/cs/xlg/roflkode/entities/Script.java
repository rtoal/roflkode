package edu.lmu.cs.xlg.roflkode.entities;

import java.util.List;

/**
 * A Roflkode script. A script is really just a top-level block. Technically a script contains
 * imports, but imports are really handled by a preprocessor. At the syntactic level, scripts are
 * statement sequences, period.
 */
public class Script extends Block {

    public Script(List<Statement> statements) {
        super(statements);
    }
}
