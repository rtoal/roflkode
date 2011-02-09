package edu.lmu.cs.xlg.roflkode;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

/**
 * A unit test for the front end of the Roflkode compiler. It reads all the ".rk" files in the test
 * resources directory. Whenever a filename starts with "synerror" the tester asserts that the
 * compiler will detect a syntax error. Whenever a name starts with "semerror" the tester asserts
 * that the compiler successfully parses the program but then detects a semantic error. Whenever a
 * file name starts with anything else, the tester checks that the compiler can successfully parse
 * and perform static analysis without finding any errors.
 */
public class FrontEndTest {

    private static final String TEST_DIRECTORY = "src/test/rk";
    private static final String EXTENSION = ".rk";

    /**
     * Tests all the files in the test directory.
     */
    @Test
    public void testFrontEnd() throws IOException {
        String[] filenames = new File(TEST_DIRECTORY).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(EXTENSION);
            }
        });

        System.out.println(filenames);

        for (String filename : filenames) {
            System.out.print("Compiling " + filename + "... ");
            Reader reader = new FileReader(TEST_DIRECTORY + "/" + filename);

            Compiler compiler = new Compiler();
            compiler.setQuiet(true);

            if (filename.startsWith("synerror")) {
                // Expect at least one error during syntax checking
                compiler.checkSyntax(reader);
                assertTrue(compiler.getErrorCount() != 0);

            } else if (filename.startsWith("semerror")) {
                // Expect no syntax errors, but one or more semantic errors
                compiler.checkSyntax(reader);
                assertTrue(compiler.getErrorCount() == 0);
                compiler.checkSemantics(reader);
                assertTrue(compiler.getErrorCount() != 0);

            } else {
                // Expect no errors even after all semantic checks
                compiler.checkSemantics(reader);
                assertTrue(compiler.getErrorCount() == 0);
            }
            System.out.println();
        }
    }
}
