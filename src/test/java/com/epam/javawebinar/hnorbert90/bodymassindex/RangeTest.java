package com.epam.javawebinar.hnorbert90.bodymassindex;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class RangeTest extends TestCase {

    Range range;

    public RangeTest() {
        range = new Range(0, 16.5);
    }

    @Test
    public void testContainsFunctionValueIsInRange() {
        assertTrue("Should in range", range.contains(0));
        assertTrue("Should in range", range.contains(16.4));
        assertFalse("Should not in range", range.contains(16.5));
        assertFalse("Should not in range", range.contains(16.51));
        assertFalse("Should not in range", range.contains(-0.1));
    }

}
