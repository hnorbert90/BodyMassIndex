package com.epam.javawebinar.hnorbert90.bodymassindex.util;

public class ConvertToKilogram {

    private final static double POUND_TO_KILOGRAM = 0.45359237;
    private final static double GRAM_TO_KILOGRAM = 0.001;
    private final static double DEKAGRAM_TO_KILOGRAM = 0.01;

    public static double convert(double value, MassUnit unit) {
        switch (unit) {
            case POUND:
                return value * POUND_TO_KILOGRAM;
            case GRAM:
                return value * GRAM_TO_KILOGRAM;
            case DEKAGRAM:
                return value * DEKAGRAM_TO_KILOGRAM;
            case KILOGRAM:
                return value;
            default:
                return value;
        }

    }

    public static double convertBack(double value, MassUnit unit) {
        switch (unit) {
            case POUND:
                return value / POUND_TO_KILOGRAM;
            case GRAM:
                return value / GRAM_TO_KILOGRAM;
            case DEKAGRAM:
                return value / DEKAGRAM_TO_KILOGRAM;
            case KILOGRAM:
                return value;
            default:
                return value;
        }

    }
}
