package edu.lmu.cs.xlg.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Simple tests on the IdGenerator.
 */
public class IdGeneratorTest {

    @Test
    public void testIds() {
        IdGenerator generator = new IdGenerator();
        assertThat(generator.id("f"), is("f0"));
        assertThat(generator.id("g"), is("g0"));
        assertThat(generator.id("dog"), is("dog0"));
        assertThat(generator.id("g"), is("g1"));
        assertThat(generator.id("g"), is("g2"));
        assertThat(generator.id("f"), is("f1"));

        // Ensure counts are not shared between generators.
        IdGenerator anotherGenerator = new IdGenerator();
        assertThat(anotherGenerator.id("f"), is("f0"));
        assertThat(generator.id("f"), is("f2"));
    }
}
