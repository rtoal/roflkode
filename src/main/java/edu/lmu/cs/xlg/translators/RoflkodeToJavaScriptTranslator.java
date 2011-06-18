package edu.lmu.cs.xlg.translators;

import java.io.PrintWriter;

import edu.lmu.cs.xlg.roflkode.entities.Script;

public class RoflkodeToJavaScriptTranslator {

    /**
     * Translates the given script, writing the target JavaSCript program to the given writer.
     */
    public static void translate(Script script, PrintWriter writer) {
        new RoflkodeToJavaScriptTranslator(writer).translateScript(script);
    }

    private PrintWriter writer;

    private RoflkodeToJavaScriptTranslator(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * Writes a JS representation of the given Roflkode script to the translator's writer.
     * Precondition: The script has already been semantically analyzed and is error-free.
     */
    private void translateScript(Script script) {
        writer.println("// Hi, this isn't done yet.");
    }

}
