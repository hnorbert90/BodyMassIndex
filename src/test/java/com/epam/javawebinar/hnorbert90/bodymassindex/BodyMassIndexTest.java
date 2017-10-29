package com.epam.javawebinar.hnorbert90.bodymassindex;


import java.util.AbstractMap.SimpleImmutableEntry;
import org.junit.Test;
import junit.framework.TestCase;


public class BodyMassIndexTest extends TestCase  {
    SimpleImmutableEntry<Double, String> actual; 
    public BodyMassIndexTest() {
        actual = BodyMassIndex.calculateBodyMassIndex(2, 80);
    }

    @Test
    public void TestCalculateBMIHasCorrectResult() {
        double expected = 20.0;
        String expectedKey = "Normal";
        assertEquals("Bad value ", expectedKey, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }

}
