package edu.lmu.cs.xlg.roflkode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import edu.lmu.cs.xlg.roflkode.entities.Script;
import edu.lmu.cs.xlg.roflkode.syntax.Parser;
import edu.lmu.cs.xlg.util.Log;

/**
 * A Roflkode compiler. This class contains a static main method allowing you to run the compiler
 * from the command line, as well as a few methods to compile, or run phases of the compiler,
 * programatically.
 */
public class Compiler {

    /**
     * A logger for logging messages (both regular and error messages). The base properties file is
     * called <code>roflkode.properties</code>.
     */
    private Log log = new Log("roflkode", new PrintWriter(System.out, true));

    /**
     * Runs the compiler as an application.
     *
     * @param args
     *            the commandline arguments. For now, the first argument should be the name of a
     *            file to compile; if missing, the compiler will read from standard input.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("All I can do now is check syntax.");

        Compiler compiler = new Compiler();
        Reader reader = (args.length == 0) ? new BufferedReader(new InputStreamReader(System.in))
                : new FileReader(args[0]);
        System.out.println(compiler.checkSyntax(reader));

        // TODO: Obviously, this is a stub.
    }

    /**
     * Checks the syntax of a Roflkode script.
     *
     * @param reader
     *            the source
     * @return the abstract syntax tree if successful, or null if there were any syntax errors
     */
    public Script checkSyntax(Reader reader) throws IOException {
        log.clearErrors();

        Parser parser = new Parser(reader);
        try {
            log.message("syntax.checking");
            return parser.parse(reader, log);
        } finally {
            reader.close();
        }
    }

    /**
     * Checks the syntax and static semantics of a Roflkode program from a file.
     *
     * @param name
     *            base name (no .carlos extension) of source file
     * @return the (checked) semantic graph if successful, or null if there were any syntax or
     *         static semantic errors
     */
    public Script checkSemantics(Reader reader) throws IOException {
        Script script = checkSyntax(reader);
        if (log.getErrorCount() > 0)
            return null;

        log.message("semantics.checking");
        // TODO -----> script.analyze(log);
        return script;
    }

    /**
     * Returns the number of errors logged so far.
     */
    public int getErrorCount() {
        return log.getErrorCount();
    }

    /**
     * Tells the compiler whether or not it should write log messages.
     */
    public void setQuiet(boolean quiet) {
        log.setQuiet(quiet);
    }
}
