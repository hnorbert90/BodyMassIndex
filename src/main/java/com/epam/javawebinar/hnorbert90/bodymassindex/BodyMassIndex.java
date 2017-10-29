package com.epam.javawebinar.hnorbert90.bodymassindex;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;

public class BodyMassIndex {

    private static Map<String, Range> bodyMassIndexTable = new HashMap<String, Range>();
    private static Map<String, Double> convertToMeters = new HashMap<String, Double>();
    private static Map<String, Double> convertToKilograms = new HashMap<String, Double>();
    static {
        bodyMassIndexTable.put("Severe Thinness", new Range(0, 16));
        bodyMassIndexTable.put("Moderate Thinness", new Range(16, 17));
        bodyMassIndexTable.put("Mild Thinness", new Range(17, 18.5));
        bodyMassIndexTable.put("Normal", new Range(18.5, 25));
        bodyMassIndexTable.put("Overweight", new Range(25, 30));
        bodyMassIndexTable.put("Obese Class I", new Range(30, 35));
        bodyMassIndexTable.put("Obese Class II", new Range(35, 40));
        bodyMassIndexTable.put("Obese Class III", new Range(40, Double.MAX_VALUE));

        convertToMeters.put("cm", 0.01);
        convertToMeters.put("centimeter", 0.01);
        convertToMeters.put("centimeters", 0.01);
        convertToMeters.put("dm", 0.01);
        convertToMeters.put("decimeter", 0.1);
        convertToMeters.put("decimeters", 0.1);
        convertToMeters.put("m", 1d);
        convertToMeters.put("meter", 1d);
        convertToMeters.put("meters", 1d);
        convertToMeters.put("mm", 0.001);
        convertToMeters.put("millimeter", 0.001);
        convertToMeters.put("milimeter", 0.001);
        convertToMeters.put("millimeters", 0.001);
        convertToMeters.put("milimeters", 0.001);
        convertToMeters.put("feet", 0.3048);
        convertToMeters.put("foot", 0.3048);
        convertToMeters.put("ft", 0.3048);
        convertToMeters.put("inch", 0.0254);
        convertToMeters.put("in", 0.0254);
        convertToMeters.put("col", 0.0254);
        convertToMeters.put("\"", 0.0254);

        convertToKilograms.put("pound", 0.45359237);
        convertToKilograms.put("pounds", 0.45359237);
        convertToKilograms.put("lbs", 0.45359237);
        convertToKilograms.put("lb", 0.45359237);
        convertToKilograms.put("gramm", 0.001);
        convertToKilograms.put("gram", 0.001);
        convertToKilograms.put("gramms", 0.001);
        convertToKilograms.put("grams", 0.001);
        convertToKilograms.put("g", 0.001);
        convertToKilograms.put("dkg", 0.01);
        convertToKilograms.put("dekagramm", 0.01);
        convertToKilograms.put("dekagram", 0.01);
        convertToKilograms.put("dekagramms", 0.01);
        convertToKilograms.put("dekagrams", 0.01);
        convertToKilograms.put("kg", 1d);
        convertToKilograms.put("kilogramm", 1d);
        convertToKilograms.put("kilogram", 1d);
        convertToKilograms.put("kilogramms", 1d);
        convertToKilograms.put("kilograms", 1d);
    }

    /**
     * Method waiting for two parameters <b>height</b>,and <b>weight</b> and returning with
     * BodyMassIndex
     * number.<br>
     * Returning unit is in <b>metric</b>
     * 
     * @param height
     * @param weight
     */
    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(double height, double weight) {
        double bodyMassIndex = Math.abs(weight) / Math.pow(Math.abs(height), 2);
        return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));
    }

    /**
     * Method waiting for two parameters <b>height</b>,and <b>weight</b> and returning with
     * BodyMassIndex
     * number.
     * Acceptable units: <br>
     * <b>Lenght</b>: mm, cm, dm, m, ft, in<br>
     * <b>Mass</b>: g, dkg, kg, lb,<br>
     * Returning unit is in <b>metric</b>
     * 
     * @param height
     * @param weight
     * @throws IllegalArgumentException
     */
    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(String height, String weight) {
        height = stringValidator(height);
        weight = stringValidator(weight);
        double bodyMassIndex = getValue(weight, convertToKilograms)
                / Math.pow(getValue(height, convertToMeters), 2);
        return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));

    }

    /**
     * Replaces commas with dot, and removes all invalid character.
     */
    private static String stringValidator(String value) {
        return value.replace(",", ".").toLowerCase().replaceAll("[^a-z&&[^0-9&&[^.]]]", "");
    }

    /**
     * Get a raw data from user which contain value and unit, this method split
     * this values, and convert this values to metric values
     * 
     * @return metric value
     * @throws IllegalArgumentException
     */
    private static double getValue(String rawValue, Map<String, Double> convertTable) {
        final String unit = getUnit(rawValue);
        if (unit.equals(""))
            return Double.parseDouble(rawValue);

        if (convertTable.entrySet()
            .stream()
            .filter(entrySet -> entrySet.getKey().equals(unit))
            .count() != 0) {
            return Double.parseDouble(rawValue.substring(0, getFirstCharIndex(rawValue)))
                    * convertTable.entrySet()
                        .stream()
                        .filter(entrySet -> entrySet.getKey().equals(unit))
                        .findFirst()
                        .get()
                        .getValue();
        } else
            throw new IllegalArgumentException("unknown unit: " + unit);
    }

    private static String getUnit(String rawValue) {
        int firstCharIndex = getFirstCharIndex(rawValue);
        String unit = "";
        if (firstCharIndex != rawValue.length())
            unit = rawValue.substring(firstCharIndex);
        return unit;
    }

    /**
     * evaluates the BodyMassIndex in which interval it falls
     */
    private static String getResults(double bodyMassIndex) {
        return bodyMassIndexTable.entrySet()
            .stream()
            .filter(set -> set.getValue().contains(bodyMassIndex))
            .findFirst()
            .get()
            .getKey();
    }

    /**
     * search for the first index which is a letter
     */
    private static int getFirstCharIndex(String rawValue) {
        // count the numbers including the point
        return (int) rawValue.chars().filter(ch -> (ch >= '0' && ch <= '9' || ch == '.')).count();
    }

}
