package edu.lmu.cs.xlg.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple generator for ids of the form xxxnnn where xxx is a user selected
 * prefix and nnn is a non-negative integer. For each prefix, ids are generated
 * with a numeric portion starting at 0.
 */
public class IdGenerator {

    // Maps each prefix to the next available integer.
    private Map<String, Integer> map = new HashMap<String, Integer>();

    /**
     * Returns a string of the form xxxnnn where xxx is the given key and nnn is
     * the value of the next available integer for the given key. Updates the
     * next available integer, of course.
     */
    public String id(String key) {
        int value = map.containsKey(key) ? map.get(key) : 0;
        map.put(key, value + 1);
        return key + value;
    }
}
