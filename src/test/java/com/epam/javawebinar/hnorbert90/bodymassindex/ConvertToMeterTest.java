package com.epam.javawebinar.hnorbert90.bodymassindex;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.epam.javawebinar.hnorbert90.bodymassindex.util.ConvertToMeter;
import com.epam.javawebinar.hnorbert90.bodymassindex.util.LengthUnit;
public class ConvertToMeterTest {

    @Test
    public void TestConvertFunction() {
        assertTrue(1.0 == ConvertToMeter.convert(100, LengthUnit.CENTIMETER));
        assertTrue(1.0 == ConvertToMeter.convert(1000, LengthUnit.MILLIMETER));
        assertTrue(1.0 == ConvertToMeter.convert(10, LengthUnit.DECIMETER));
        assertTrue(0.3048 == ConvertToMeter.convert(1, LengthUnit.FEET));
        assertTrue(0.0254 == ConvertToMeter.convert(1, LengthUnit.INCH));
        assertTrue(0.9144 == ConvertToMeter.convert(1, LengthUnit.YARD));
    }
    
    @Test
    public void TestConvertBackFunction() {
        assertTrue(100 == ConvertToMeter.convertBack(ConvertToMeter.convert(100, LengthUnit.CENTIMETER),LengthUnit.CENTIMETER));
    }


}
