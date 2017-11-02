package com.epam.javawebinar.hnorbert90.bodymassindex;

import static java.lang.Math.pow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.epam.javawebinar.hnorbert90.bodymassindex.model.BodyMassIndexCategory;
import com.epam.javawebinar.hnorbert90.bodymassindex.model.Range;
import com.epam.javawebinar.hnorbert90.bodymassindex.util.*;
import com.epam.javawebinar.hnorbert90.exception.*;

/**
 * <br>
 * <b>Description:</b>
 * <br>
 * <br>
 * <i>The Body Mass Index (BMI) Calculator can be used to calculate BMI
 * <br>
 * value and corresponding weight status while taking age into consideration.<i>
 * <br>
 * 
 * @author Norbert Hollay
 */
public class BodyMassIndex {

    private static LengthUnit lengthUnit;
    private static MassUnit massUnit;
    private final static Map<Range, Map<String, Range>> CHILD_GROWN_TABLE_MALE =
            new HashMap<Range, Map<String, Range>>();
    private final static Map<Range, Map<String, Range>> CHILD_GROWN_TABLE_FEMALE =
            new HashMap<Range, Map<String, Range>>();
    private final static ArrayList<BodyMassIndexCategory> CATEGORIES = new ArrayList<>();
    private final static double CHILD_MAX_AGE = 19.9;
    private final static DecimalFormat DF = new DecimalFormat("#.##");
    private final static String BODY_MASS_INDEX_UNIT = "kg/m^2";

    static {
        CATEGORIES.add(new BodyMassIndexCategory("Severe Thinness", 0, 16));
        CATEGORIES.add(new BodyMassIndexCategory("Moderate Thinness", 16, 17));
        CATEGORIES.add(new BodyMassIndexCategory("Mild Thinness", 17, 18.5));
        CATEGORIES.add(new BodyMassIndexCategory("Normal", 18.5, 25));
        CATEGORIES.add(new BodyMassIndexCategory("Overweight", 25, 30));
        CATEGORIES.add(new BodyMassIndexCategory("Obese Class I", 30, 35));
        CATEGORIES.add(new BodyMassIndexCategory("Obese Class II", 35, 40));
        CATEGORIES.add(new BodyMassIndexCategory("Obese Class III", 40, Double.MAX_VALUE));
        loadChildGrownTable();
    }

    public static ArrayList<BodyMassIndexCategory> getCategories() {
        return CATEGORIES;
    }

    public static String getBodyMassIndexUnit() {
        return BODY_MASS_INDEX_UNIT;
    }

    /**
     * @param height <i>(default unit in Meter)</i>
     * @param weight <i>(default unit in Kilogram)</i>
     * @param age
     * @param gender <i>(Male="M", Female="F")</i>
     * @return BMI value and corresponding weight status while taking age into consideration
     * @throws InvalidInputException
     * @throws InvalidLengthUnitException
     * @throws InvalidMassUnitException
     */
    public static String calculateBodyMassIndex(double height, double weight, double age,
            String gender)
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        return calculateBodyMassIndex(height, "METER", weight, "KILOGRAM", age, gender);
    }

    /**
     * @param height <i>(default unit in Meter)</i>
     * @param heightUnit <i>(Meter,Millimeter,Decimeter,Centimeter,Feet,Inch,Yard)</i>
     * @param weight <i>(default unit in Kilogram)</i>
     * @param weightUnit <i>(Kilogram,Gram,Dekagram,Pound)</i>
     * @param age
     * @param gender <i>(Male="M", Female="F")</i>
     * @return BMI value and corresponding weight status while taking age into consideration
     * @throws InvalidLengthUnitException
     * @throws InvalidMassUnitException
     * @throws InvalidInputException
     */
    public static String calculateBodyMassIndex(double height, String heightUnit, double weight,
            String weightUnit, double age, String gender)
            throws InvalidLengthUnitException, InvalidMassUnitException, InvalidInputException {
        validateHeight(height);
        validateWeight(weight);
        validateAge(age);
        validateGender(gender);
        getLengthUnit(heightUnit);
        getMassUnit(weightUnit);
        double unifiedHeight = ConvertToMeter.convert(height, lengthUnit);
        double unifiedWeight = ConvertToKilogram.convert(weight, massUnit);
        double bodyMassIndex = unifiedWeight / pow(unifiedHeight, 2);

        if (age > CHILD_MAX_AGE) {
            return getResultAdult(bodyMassIndex, unifiedHeight, unifiedWeight);
        } else {
            return getResultChild(bodyMassIndex, unifiedHeight, unifiedWeight, age, gender);
        }
    }

    private static void validateHeight(double height) throws InvalidInputException {
        if (height <= 0) {
            throw new InvalidInputException("Please provide positive height value.");
        }
    }

    private static void validateWeight(double weight) throws InvalidInputException {
        if (weight <= 0) {
            throw new InvalidInputException("Please provide positive weight value.");
        }
    }

    private static void validateAge(double age) throws InvalidInputException {
        if (age < 2 || age >= 120) {
            throw new InvalidInputException("Please provide an age between 2 and 120.");
        }

    }

    private static void validateGender(String gender) throws InvalidInputException {
        if (Objects.isNull(gender)
                || !(gender.toUpperCase().equals("F") || gender.toUpperCase().equals("M"))) {
            throw new InvalidInputException("Please provide valid gender value! (F or M)");
        }

    }

    private static void getLengthUnit(String heightUnit) throws InvalidLengthUnitException {
        try {
            lengthUnit = Objects.nonNull(heightUnit) && !heightUnit.equals("") ? LengthUnit
                .valueOf(heightUnit.toUpperCase()) : LengthUnit.METER;
        } catch (IllegalArgumentException e) {
            throw new InvalidLengthUnitException();
        }
    }

    private static void getMassUnit(String weightUnit) throws InvalidMassUnitException {
        try {
            massUnit = Objects.nonNull(weightUnit) && !weightUnit.equals("") ? MassUnit
                .valueOf(weightUnit.toUpperCase()) : MassUnit.KILOGRAM;
        } catch (IllegalArgumentException e) {
            throw new InvalidMassUnitException();
        }
    }

    private static String getResultAdult(double bodyMassIndex, double height, double weight) {
        BodyMassIndexCategory normal =
                CATEGORIES.stream().filter(a -> a.getCategory().equals("Normal")).findFirst().get();
        double minWeight = normal.getMin() * pow(height, 2);
        double maxWeight = normal.getMax() * pow(height, 2);
        StringBuilder sb = new StringBuilder(512);
        sb.append("Your body mass index is: " + DF.format(bodyMassIndex) + " "
                + BODY_MASS_INDEX_UNIT);
        sb.append(" (" + evalulateBodyMassIndex(bodyMassIndex) + ")");
        sb.append("\n");
        sb.append(normal.toString());
        sb.append("\n");
        sb.append("Normal BMI weight range for the height: ");
        sb.append(ConvertToKilogram.convertBack(minWeight, massUnit) + " "
                + massUnit.toString().toLowerCase());
        sb.append(" - ");
        sb.append(ConvertToKilogram.convertBack(maxWeight, massUnit) + " "
                + massUnit.toString().toLowerCase());
        if (bodyMassIndex > normal.getMax()) {
            sb.append("\nYou will need to loss "
                    + DF.format(ConvertToKilogram.convertBack((weight - maxWeight), massUnit)) + " "
                    + massUnit.toString().toLowerCase() + " to reach a BMI of " + normal.getMax()
                    + BODY_MASS_INDEX_UNIT);
        } else if (bodyMassIndex < normal.getMin()) {
            sb.append("\nYou will need to gain "
                    + DF.format(ConvertToKilogram.convertBack((minWeight - weight), massUnit)) + " "
                    + massUnit.toString().toLowerCase() + " to reach a BMI of " + normal.getMin()
                    + BODY_MASS_INDEX_UNIT);
        }
        sb.append(".");
        return sb.toString();
    }

    private static String getResultChild(double bodyMassIndex, double unifiedHeight,
            double unifiedWeight, double age, String gender) {
        Set<Entry<String, Range>> bmiTable = getChildBodyMassIndexTable(gender, age);
        Entry<String, Range> normal = bmiTable.stream()
            .filter((k) -> k.getKey().equals("Healthy weight"))
            .findFirst()
            .get();
        String category = bmiTable.stream()
            .filter(k -> k.getValue().contains(bodyMassIndex))
            .findFirst()
            .get()
            .getKey();
        Range normalCategory = normal.getValue();
        double minWeight = normal.getValue().getMin() * pow(unifiedHeight, 2);
        double maxWeight = normal.getValue().getMax() * pow(unifiedHeight, 2);

        StringBuilder sb = new StringBuilder(512);
        sb.append("Your body mass index is: " + new DecimalFormat("#.##").format(bodyMassIndex)
                + " " + BODY_MASS_INDEX_UNIT);
        sb.append(" (" + category + ")");
        sb.append("\n");
        sb.append("Normal BMI range: " + DF.format(normalCategory.getMin()) + " kg/m^2 - "
                + DF.format(normalCategory.getMax()) + " kg/m 2");
        sb.append("\n");
        sb.append("Normal BMI weight range for the height: ");
        sb.append(DF.format(ConvertToKilogram.convertBack(minWeight, massUnit)) + " "
                + massUnit.toString().toLowerCase());
        sb.append(" - ");
        sb.append(DF.format(ConvertToKilogram.convertBack(maxWeight, massUnit)) + " "
                + massUnit.toString().toLowerCase());
        if (bodyMassIndex > normalCategory.getMax()) {
            sb.append("\nYou will need to loss "
                    + DF.format(
                            ConvertToKilogram.convertBack((unifiedWeight - maxWeight), massUnit))
                    + " " + massUnit.toString().toLowerCase() + " to reach a BMI of "
                    + DF.format(normalCategory.getMax()) + BODY_MASS_INDEX_UNIT);
        } else if (bodyMassIndex < normalCategory.getMin()) {
            sb.append("\nYou will need to gain "
                    + DF.format(
                            ConvertToKilogram.convertBack((minWeight - unifiedWeight), massUnit))
                    + " " + massUnit.toString().toLowerCase() + " to reach a BMI of "
                    + DF.format(normalCategory.getMin()) + BODY_MASS_INDEX_UNIT);
        }
        sb.append(".");
        return sb.toString();

    }

    private static String evalulateBodyMassIndex(double bodyMassIndex) {
        return CATEGORIES.stream()
            .filter(a -> a.contains(bodyMassIndex))
            .findFirst()
            .get()
            .getCategory();
    }

    private static void loadChildGrownTable() {
        String fileName = "data/CDC_data.txt";
        Map<Range, Map<String, Range>> actualTable;

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            Object[] rows = stream.toArray();
            String[][] table = new String[rows.length][];

            HashMap<String, Range> cache = new HashMap<String, Range>();
            for (int i = 0; i < rows.length; i++)
                table[i] = rows[i].toString().split(";");

            for (int i = 1; i < table.length - 1; i++) {
                cache = new HashMap<String, Range>();
                actualTable = table[i][CDCTable.GENDER.getIndex()]
                    .equals("F") ? CHILD_GROWN_TABLE_FEMALE : CHILD_GROWN_TABLE_MALE;

                cache.put("Underweight", new Range(0,
                        Double.parseDouble((String) table[i][CDCTable.BMI_5.getIndex()])));

                cache.put("Healthy weight",
                        new Range(Double.parseDouble((String) table[i][CDCTable.BMI_5.getIndex()]),
                                Double.parseDouble((String) table[i][CDCTable.BMI_85.getIndex()])));

                cache.put("At risk of overweight",
                        new Range(Double.parseDouble((String) table[i][CDCTable.BMI_85.getIndex()]),
                                Double.parseDouble((String) table[i][CDCTable.BMI_95.getIndex()])));

                cache.put("Overweight",
                        new Range(Double.parseDouble((String) table[i][CDCTable.BMI_95.getIndex()]),
                                Double.MAX_VALUE));

                actualTable.put(
                        new Range(Double.parseDouble((String) table[i][CDCTable.AGE.getIndex()]),
                                Double.parseDouble((String) table[i + 1][CDCTable.AGE.getIndex()])),
                        cache);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<Entry<String, Range>> getChildBodyMassIndexTable(String gender, double age) {
        Map<Range, Map<String, Range>> actualTable;
        actualTable = gender.equals("F") ? CHILD_GROWN_TABLE_FEMALE : CHILD_GROWN_TABLE_MALE;
        return actualTable.entrySet()
            .stream()
            .filter(a -> a.getKey().contains(age))
            .findFirst()
            .get()
            .getValue()
            .entrySet();
    }
}
