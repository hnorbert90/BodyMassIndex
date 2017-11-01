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
import java.util.Set;
import java.util.stream.Stream;

import com.epam.javawebinar.hnorbert90.bodymassindex.model.BodyMassIndexCategory;
import com.epam.javawebinar.hnorbert90.bodymassindex.model.Range;
import com.epam.javawebinar.hnorbert90.bodymassindex.util.*;
import com.epam.javawebinar.hnorbert90.exception.*;




/**
 * @author Norbi
 *
 */
public class BodyMassIndex {

    private final static double CHILD_MAX_AGE = 19.9;
    private static DecimalFormat df = new DecimalFormat("#.##");
    private static Map<Range, Map<String, Range>> childGrownTableMale =
            new HashMap<Range, Map<String, Range>>();
    private static Map<Range, Map<String, Range>> childGrownTableFemale =
            new HashMap<Range, Map<String, Range>>();
    public final static String BODY_MASS_INDEX_UNIT = "kg/m^2";
    public static LengthUnit lengthUnit;
    public static MassUnit massUnit;
    public final static ArrayList<BodyMassIndexCategory> CATEGORIES = new ArrayList<>();
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

    public static String calculateBodyMassIndex(double height, double weight,double age,String gender)
            throws InvalidInputException, InvalidLengthUnitException, InvalidMassUnitException {
        return calculateBodyMassIndex(height, "METER", weight, "KILOGRAM",age,gender);
    }

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
        if (height < 0) {
            throw new InvalidInputException("Please provide positive height value.");
        }
    }

    private static void validateWeight(double weight) throws InvalidInputException {
        if (weight < 0) {
            throw new InvalidInputException("Please provide positive weight value.");
        }
    }

    private static void validateAge(double age) throws InvalidInputException {
        if (age < 2 || age >= 120) {
            throw new InvalidInputException("Please provide an age between 2 and 120.");
        }

    }

    private static void validateGender(String gender) throws InvalidInputException {
        if (!(gender.toUpperCase().equals("F") || gender.toUpperCase().equals("M"))) {
            throw new InvalidInputException("Please provide valid gender value! (F or M)");
        }

    }

    private static void getLengthUnit(String heightUnit) throws InvalidLengthUnitException {
        try {
            lengthUnit = !heightUnit.equals("") ? LengthUnit
                .valueOf(heightUnit.toUpperCase()) : LengthUnit.METER;
        } catch (IllegalArgumentException e) {
            throw new InvalidLengthUnitException();
        }
    }

    private static void getMassUnit(String weightUnit) throws InvalidMassUnitException {
        try {
            massUnit = !weightUnit.equals("") ? MassUnit
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
        sb.append("Your body mass index is: " + df.format(bodyMassIndex) + " "
                + BODY_MASS_INDEX_UNIT);
        sb.append(" (" + evalulateBodyMassIndex(bodyMassIndex) + ")");
        sb.append("\n");
        sb.append(normal.toString());
        sb.append("\n");
        sb.append("Normal BMI weight range for the height: ");
        sb.append(ConvertToKilogram.convertBack(minWeight, massUnit) + " " + massUnit.toString().toLowerCase());
        sb.append(" - ");
        sb.append(ConvertToKilogram.convertBack(maxWeight, massUnit) + " " + massUnit.toString().toLowerCase());
        if (bodyMassIndex > normal.getMax()) {
            sb.append("\nYou will need to loss " + df.format(ConvertToKilogram.convertBack((weight - maxWeight), massUnit)) + " "
                    + massUnit.toString().toLowerCase() + " to reach a BMI of " + normal.getMax()
                    + BODY_MASS_INDEX_UNIT);
        } else if (bodyMassIndex < normal.getMin()) {
            sb.append("\nYou will need to gain " + df.format(ConvertToKilogram.convertBack((minWeight - weight), massUnit)) + " "
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
        sb.append("Normal BMI range: " + df.format(normalCategory.getMin()) + " kg/m^2 - "
                + df.format(normalCategory.getMax()) + " kg/m 2");
        sb.append("\n");
        sb.append("Normal BMI weight range for the height: ");
        sb.append(df.format(ConvertToKilogram.convertBack(minWeight, massUnit)) + " " + massUnit.toString().toLowerCase());
        sb.append(" - ");
        sb.append(df.format(ConvertToKilogram.convertBack(maxWeight, massUnit)) + " " + massUnit.toString().toLowerCase());
        if (bodyMassIndex > normalCategory.getMax()) {
            sb.append("\nYou will need to loss " + df.format(ConvertToKilogram.convertBack((unifiedWeight - maxWeight), massUnit)) + " "
                    + massUnit.toString().toLowerCase() + " to reach a BMI of "
                    + df.format(normalCategory.getMax()) + BODY_MASS_INDEX_UNIT);
        } else if (bodyMassIndex < normalCategory.getMin()) {
            sb.append("\nYou will need to gain " + df.format(ConvertToKilogram.convertBack((minWeight - unifiedWeight), massUnit)) + " "
                    + massUnit.toString().toLowerCase() + " to reach a BMI of "
                    + df.format(normalCategory.getMin()) + BODY_MASS_INDEX_UNIT);
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

    public static void loadChildGrownTable() {
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
                    .equals("F") ? childGrownTableFemale : childGrownTableMale;

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

    public static Set<Entry<String, Range>> getChildBodyMassIndexTable(String gender, double age) {
        Map<Range, Map<String, Range>> actualTable;
        actualTable = gender.equals("F") ? childGrownTableFemale : childGrownTableMale;
        return actualTable.entrySet()
            .stream()
            .filter(a -> a.getKey().contains(age))
            .findFirst()
            .get()
            .getValue()
            .entrySet();
    }
}
