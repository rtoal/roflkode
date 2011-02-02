package edu.lmu.cs.xlg.roflkode;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import edu.lmu.cs.xlg.roflkode.entities.Script;
import edu.lmu.cs.xlg.roflkode.syntax.Parser;
import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode Compiler.
 */
public class Compiler {

    // A logger for logging messages (both regular and error messages).
    // The base properties file is called <code>Carlos.properties</code>.
    private Log log = new Log("Carlos", new PrintWriter(System.out, true));

    /**
     * Runs the compiler as an application.
     */
    public static void main(String[] args) {
        System.out.println("Hello, this is the compiler.");
    }

    /**
     * Checks the syntax of a ROFLKODE program from a file.
     *
     * @param
     *     name base name (no .rk extension) of source file
     * @return
     *     the abstract syntax tree if successful, or null if there were any syntax
     *     errors
     */
    public Script checkSyntax(String name) throws IOException {
        log.clearErrors();

        Reader reader = new FileReader(name + ".rk");
        Parser parser = new Parser(reader);
        try {
            log.message("checking_syntax");
            return parser.parse(reader, log);
        } finally {
            reader.close();
        }
    }


}
