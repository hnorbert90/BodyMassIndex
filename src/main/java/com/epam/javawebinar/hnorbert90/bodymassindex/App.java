package com.epam.javawebinar.hnorbert90.bodymassindex;

import static com.epam.javawebinar.hnorbert90.bodymassindex.BodyMassIndex.calculateBodyMassIndex;

import com.epam.javawebinar.hnorbert90.exception.InvalidInputException;
import com.epam.javawebinar.hnorbert90.exception.InvalidLengthUnitException;
import com.epam.javawebinar.hnorbert90.exception.InvalidMassUnitException;

public class App {

    public static void main(String[] args)
            throws InvalidLengthUnitException, InvalidMassUnitException, InvalidInputException {

        String gender = "F";
        double height = 15;
        double weight = 15;
        double age = 16;
        String lenghtUnit = null;
        String massUnit = null;
        System.out
            .println(calculateBodyMassIndex(height, lenghtUnit, weight, massUnit, age, gender));
    }

}
