package com.epam.javawebinar.hnorbert90.bodymassindex;

import static com.epam.javawebinar.hnorbert90.bodymassindex.BodyMassIndex.calculateBodyMassIndex;

import com.epam.javawebinar.hnorbert90.exception.InvalidInputException;
import com.epam.javawebinar.hnorbert90.exception.InvalidLengthUnitException;
import com.epam.javawebinar.hnorbert90.exception.InvalidMassUnitException;

public class App {
	public static void main(String[] args) throws InvalidLengthUnitException, InvalidMassUnitException, InvalidInputException {

            System.out.println(calculateBodyMassIndex(1.75, "meter", 80, "kilogram", 15, "M"));
	    
	}
	
}
