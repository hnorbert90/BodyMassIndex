package com.epam.javawebinar.hnorbert90.bodymassindex;

import static com.epam.javawebinar.hnorbert90.bodymassindex.BodyMassIndex.calculateBodyMassIndex;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.epam.javawebinar.hnorbert90.exception.InvalidInputException;
import com.epam.javawebinar.hnorbert90.exception.InvalidLengthUnitException;
import com.epam.javawebinar.hnorbert90.exception.InvalidMassUnitException;
public class BodyMassIndexTest {

    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionNegativeHeight()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(-1.75, 80, 15, "M");
    }

    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionNegativeWeight()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, -80, 15, "M");
    }

    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionNegativeAge()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "BadMass", -5, "M");
    }

    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionTooHighValue()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "BadMass", 120, "M");
    }
    
    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionTooLowValue()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "BadMass", 1, "M");
    }
    
    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidInputExceptionWrongGender()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "kilogram", 5, "BadGender");
    }

    @Test(expected = InvalidLengthUnitException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidLengthUnitException()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "BadLength", 80, "Kilogram", 15, "M");
    }

    @Test(expected = InvalidMassUnitException.class)
    public void TestCalculateBodyMassIndexFunctionInvalidMassUnitException()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "BadMass", 15, "M");
    }
    
    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionNullGender()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 80, "kilogram", 15, null);
    }
    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionZeroMass()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(1.75, "meter", 0, "kilogram", 15, "F");
    }
    @Test(expected = InvalidInputException.class)
    public void TestCalculateBodyMassIndexFunctionZeroHeight()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        calculateBodyMassIndex(0, "meter", 80, "kilogram", 15, "F");
    }
    
    @Test()
    public void TestCalculateBodyMassIndexFunctionForNullUnitsShouldUseMetricUnits()
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        String result = calculateBodyMassIndex(1.75, null, 80, null, 15, "M");
        assertTrue("Expected bmi",result.contains("26,12"));
        assertTrue("Expected category",result.contains("At risk of overweight"));
        assertTrue("Expected weight You will need to loss",result.contains("8,38"));
    }
    
    
    @Test
    public void TestCalculateBodyMassIndexFunction() throws InvalidLengthUnitException, InvalidMassUnitException, InvalidInputException {
        String result=calculateBodyMassIndex(1.75, "meter", 80, "kilogram", 15, "M");
        assertTrue("Expected bmi",result.contains("26,12"));
        assertTrue("Expected category",result.contains("At risk of overweight"));
        assertTrue("Expected weight You will need to loss",result.contains("8,38"));
        
        result=calculateBodyMassIndex(1750, "millimeter", 80000, "gram", 15, "M");
        assertTrue("Expected bmi",result.contains("26,12"));
        assertTrue("Expected category",result.contains("At risk of overweight"));
        assertTrue("Expected weight You will need to loss",result.contains("8378,63"));
        
        result=calculateBodyMassIndex(6, "feet", 130, "pound", 15, "M");
        assertTrue("Expected bmi",result.contains("17,63"));
        assertTrue("Expected category",result.contains("Healthy weight"));
        
    }

}
