package com.epam.javawebinar.hnorbert90.bodymassindex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import com.epam.javawebinar.hnorbert90.bodymassindex.model.Range;


public class RangeTest {

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
