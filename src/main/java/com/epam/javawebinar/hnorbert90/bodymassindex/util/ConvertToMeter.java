package com.epam.javawebinar.hnorbert90.bodymassindex.util;

public class ConvertToMeter {

    private final static double FEET_TO_METER = 0.3048;
    private final static double INCH_TO_METER = 0.0254;
    private final static double YARD_TO_METER = 0.9144;
    private final static double MILLIMETER_TO_METER = 0.001;
    private final static double CENTIMETER_TO_METER = 0.01;
    private final static double DECIMETER_TO_METER = 0.1;

    public static double convert(double value, LengthUnit unit) {
        switch (unit) {
            case FEET:
                return value * FEET_TO_METER;
            case INCH:
                return value * INCH_TO_METER;
            case YARD:
                return value * YARD_TO_METER;
            case MILLIMETER:
                return value * MILLIMETER_TO_METER;
            case CENTIMETER:
                return value * CENTIMETER_TO_METER;
            case DECIMETER:
                return value * DECIMETER_TO_METER;
            case METER:
                return value;
            default:
                return value;
        }
    }

    public static double convertBack(double value, LengthUnit unit) {
        switch (unit) {
            case FEET:
                return value / FEET_TO_METER;
            case INCH:
                return value / INCH_TO_METER;
            case YARD:
                return value / YARD_TO_METER;
            case MILLIMETER:
                return value / MILLIMETER_TO_METER;
            case CENTIMETER:
                return value / CENTIMETER_TO_METER;
            case DECIMETER:
                return value / DECIMETER_TO_METER;
            case METER:
                return value;
            default:
                return value;
        }
    }
}
