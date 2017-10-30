package com.epam.javawebinar.hnorbert90.bodymassindex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class BodyMassIndex {

    private static Map<String, Range> bodyMassIndexTable = new HashMap<String, Range>();
    private static Map<String, Double> convertToMeters = new HashMap<String, Double>();
    private static Map<String, Double> convertToKilograms = new HashMap<String, Double>();
    private static Map<Range, Map<String, Range>> childGrownTableMale =
            new HashMap<Range, Map<String, Range>>();
    private static Map<Range, Map<String, Range>> childGrownTableFemale =
            new HashMap<Range, Map<String, Range>>();
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
        loadChildGrownTable();
    }

    /**
     * Method waiting for two parameters <b>height</b>,and <b>weight</b> and returning with
     * BodyMassIndex
     * number.<br>
     * Returning unit is in <b>metric</b>
     * 
     * @param height
     * @param weight
     * @throws IllegalArgumentException
     */
    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(double height,
            double weight) {
        double bodyMassIndex = Math.abs(weight) / Math.pow(Math.abs(height), 2);
        return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));
    }

    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(double height,
            double weight, double age, String gender) {
        gender = filterInput(gender);
        validateAgeAndGender(age, gender);
        double bodyMassIndex = Math.abs(weight) / Math.pow(Math.abs(height), 2);
        if (age > 20)
            return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));
        else
            return new SimpleImmutableEntry<>(bodyMassIndex,
                    getChildBodyMassIndexResults(gender, age, bodyMassIndex));
    }

    private static void validateAgeAndGender(double age, String gender) {
        if (!(age >= 2 && age < 120))
            throw new IllegalArgumentException("Please provide an age between 2 and 120!");
        if (!(gender.equals("F") || gender.equals("M")))
            throw new IllegalArgumentException("Please provide a valid gender!");
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
    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(String height,
            String weight) {
        height = stringValidator(height);
        weight = stringValidator(weight);
        double bodyMassIndex = getValue(weight, convertToKilograms)
                / Math.pow(getValue(height, convertToMeters), 2);
        return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));
    }

    public static SimpleImmutableEntry<Double, String> calculateBodyMassIndex(String height,
            String weight, double age, String gender) {
        height = stringValidator(height);
        weight = stringValidator(weight);
        gender = filterInput(gender);
        validateAgeAndGender(age, gender);
        double bodyMassIndex = getValue(weight, convertToKilograms)
                / Math.pow(getValue(height, convertToMeters), 2);
        if (age > 20)
            return new SimpleImmutableEntry<>(bodyMassIndex, getResults(bodyMassIndex));
        else
            return new SimpleImmutableEntry<>(bodyMassIndex,
                    getChildBodyMassIndexResults(gender, age, bodyMassIndex));
    }

    /**
     * Replaces commas with dot, and removes all invalid character.
     */
    private static String stringValidator(String value) {
        return value.replace(",", ".").toLowerCase().replaceAll("[^a-z&&[^0-9&&[^.]]]", "");
    }

    private static String filterInput(String gender) {
        try {
            return gender.toUpperCase().replaceAll("[^F&&[^M]]", "").substring(0, 1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Please provide a valid gender!");
        }
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

    private static void loadChildGrownTable() {
        String fileName = "data/CDC_data.csv";
        Map<Range, Map<String, Range>> actualTable;

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            Object[] table = stream.filter(a -> a.contains("<td>"))
                .map(a -> a.replaceAll("[^0-9&&[^.&&[^F&&[^M]]]]", ""))
                .toArray();
            HashMap<String, Range> cache = new HashMap<String, Range>();
            // reading starts from 2 year old
            for (int i = 50 * 30; i < table.length - 30; i += 30) {
                cache = new HashMap<String, Range>();
                actualTable = table[i].equals("F") ? childGrownTableFemale : childGrownTableMale;
                cache.put("Underweight",
                        new Range(0, OptionalDouble.of(Double.parseDouble((String) table[i + 21]))
                            .getAsDouble()));
                cache.put("Healthy weight",
                        new Range(
                                OptionalDouble.of(Double.parseDouble((String) table[i + 21]))
                                    .getAsDouble(),
                                OptionalDouble.of(Double.parseDouble((String) table[i + 26]))
                                    .getAsDouble()));
                cache.put("At risk of overweight",
                        new Range(
                                OptionalDouble.of(Double.parseDouble((String) table[i + 26]))
                                    .getAsDouble(),
                                OptionalDouble.of(Double.parseDouble((String) table[i + 28]))
                                    .getAsDouble()));
                cache.put("Overweight", new Range(
                        OptionalDouble.of(Double.parseDouble((String) table[i + 28])).getAsDouble(),
                        Double.MAX_VALUE));
                actualTable.put(new Range(
                        OptionalDouble.of(Double.parseDouble((String) table[i + 1])).getAsDouble(),
                        OptionalDouble.of(Double.parseDouble((String) table[i + 31]))
                            .getAsDouble()),
                        cache);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getChildBodyMassIndexResults(String gender, double age,
            double bodyMassIndex) {
        Map<Range, Map<String, Range>> actualTable;
        actualTable = gender.equals("F") ? childGrownTableFemale : childGrownTableMale;
        return actualTable.entrySet()
            .stream()
            .filter(a -> a.getKey().contains(age))
            .findFirst()
            .get()
            .getValue()
            .entrySet()
            .stream()
            .filter(a -> a.getValue().contains(bodyMassIndex))
            .findFirst()
            .get()
            .getKey();
    }

}
