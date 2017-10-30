package com.epam.javawebinar.hnorbert90.bodymassindex;


import static org.junit.Assert.assertEquals;

import java.util.AbstractMap.SimpleImmutableEntry;
import org.junit.Test;


public class BodyMassIndexTest {
    SimpleImmutableEntry<Double, String> actual; 
    public BodyMassIndexTest() {
        actual = BodyMassIndex.calculateBodyMassIndex(2, 80);
    }

    @Test
    public void TestCalculateBodyMassIndexHasCorrectResult() {
        Double expected = 20.0;
        String expectedValue = "Normal";
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }
    @Test
    public void TestCalculateBodyMassIndexCanHandleNegativeValues() {
        actual = BodyMassIndex.calculateBodyMassIndex(-2, 80);
        Double expected = 20.0;
        String expectedValue = "Normal";
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }
    
    @Test
    public void TestCalculateBodyMassIndexCanHandleStringUnits() {
        actual = BodyMassIndex.calculateBodyMassIndex("2m", "80kg");
        Double expected = 20.0;
        String expectedValue = "Normal";
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
        
        actual = BodyMassIndex.calculateBodyMassIndex("2000mm", "80000g");
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
        
        actual = BodyMassIndex.calculateBodyMassIndex("200cm", "8000dkg");
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }
    
    @Test
    public void TestCalculateBodyMassIndexCanHandleSpecialCharacters() {
        actual = BodyMassIndex.calculateBodyMassIndex("!2 -m", "8[0 k@g");
        Double expected = 20.0;
        String expectedValue = "Normal";
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }
    
    @Test
    public void TestCalculateBodyMassIndexCanHandleANSIUnits() {
        actual = BodyMassIndex.calculateBodyMassIndex("6feet","176pound");
        Double expected = 23.869646222317133;
        String expectedValue = "Normal";
        assertEquals("Bad value ", expectedValue, actual.getValue());
        assertEquals("Bad Key", expected, actual.getKey());
    }

}
