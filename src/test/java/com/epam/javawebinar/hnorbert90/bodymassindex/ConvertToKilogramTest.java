package com.epam.javawebinar.hnorbert90.bodymassindex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.epam.javawebinar.hnorbert90.bodymassindex.util.ConvertToKilogram;
import com.epam.javawebinar.hnorbert90.bodymassindex.util.MassUnit;
public class ConvertToKilogramTest {

    @Test
    public void TestConvertFunction() {
        assertTrue(1.0 == ConvertToKilogram.convert(100, MassUnit.DEKAGRAM));
        assertTrue(1.0 == ConvertToKilogram.convert(1000, MassUnit.GRAM));
        assertTrue(0.45359237 == ConvertToKilogram.convert(1, MassUnit.POUND));
    }
    
    @Test
    public void TestConvertBackFunction() {
        assertFalse(100 == ConvertToKilogram.convertBack(ConvertToKilogram.convert(100, MassUnit.GRAM),MassUnit.DEKAGRAM));
        assertTrue(100 == ConvertToKilogram.convertBack(ConvertToKilogram.convert(100, MassUnit.DEKAGRAM),MassUnit.DEKAGRAM));
    }


}
