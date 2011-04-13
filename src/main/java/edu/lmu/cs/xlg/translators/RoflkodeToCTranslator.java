package edu.lmu.cs.xlg.translators;

import java.util.List;

import edu.lmu.cs.xlg.roflkode.entities.BukkitType;
import edu.lmu.cs.xlg.roflkode.entities.Script;

public class RoflkodeToCTranslator {

    /**
     * Returns a string representation, in the language C, of the given Roflkode script.
     * Precondition: The script has already been semantically analyzed and is error-free.
     */
    public String toC(Script s) {

        StringBuilder builder = new StringBuilder();

        // First fetch all the types.  Render declarations then definitions.

        // Next fetch all the functions.  Render signatures then bodies.  Note that externs will
        // of course have only signatures.

        // Finally put the statements of the top-level statements in the main() function.

        builder.append("O NOES IZNT REDDY YET");

        return builder.toString();
    }

    private List<BukkitType> fetchAllBukkitTypes(StringBuilder builder) {
        // TODO stub
        return null;
    }
}
