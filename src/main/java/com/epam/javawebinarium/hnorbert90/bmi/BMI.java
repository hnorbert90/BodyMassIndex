package com.epam.javawebinarium.hnorbert90.bmi;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class BMI {

	private static Map<String, Range> bmiTable = new HashMap<String, Range>();
	private static Map<String, Double> convertToMeters = new HashMap<String, Double>();
	private static Map<String, Double> convertToKilograms = new HashMap<String, Double>();
	private static DecimalFormat df = new DecimalFormat("##0.###");
	static {
		bmiTable.put("Severe Thinness", new Range(0, 16));
		bmiTable.put("Moderate Thinness", new Range(16, 17));
		bmiTable.put("Mild Thinness", new Range(17, 18.5));
		bmiTable.put("Normal", new Range(18.5, 25));
		bmiTable.put("Overweight", new Range(25, 30));
		bmiTable.put("Obese Class I", new Range(30, 35));
		bmiTable.put("Obese Class II", new Range(35, 40));
		bmiTable.put("Obese Class III", new Range(40, Double.MAX_VALUE));

		convertToMeters.put("cm", 0.01);
		convertToMeters.put("centimeter", 0.01);
		convertToMeters.put("centimeters", 0.01);
		convertToMeters.put("m", 1d);
		convertToMeters.put("meter", 1d);
		convertToMeters.put("meters", 1d);
		convertToMeters.put("mm", 0.001);
		convertToMeters.put("millimeter", 0.001);
		convertToMeters.put("milimeter", 0.001);
		convertToMeters.put("millimeters", 0.001);
		convertToMeters.put("milimeters", 0.001);
		convertToMeters.put("feet", 0.3048);
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

	public static String calculateBMI(double height, double weight) {
		double bmi = weight / Math.pow(height, 2);
		return getResults(bmi) + "! Your BMI value is: " + df.format(bmi) + "kg/m^2";
	}

	/**
	 * 
	 * Method waiting for two parameters height, weight and returning with BMI
	 * number.
	 * 
	 * @throws IllegalArgumentException
	 */

	public static String calculateBMI(String height, String weight) {
		height = stringValidator(height);
		weight = stringValidator(weight);

		double bmi = getValue(weight, convertToKilograms) / Math.pow(getValue(height, convertToMeters), 2);
		return getResults(bmi) + "! Your BMI value is: " + df.format(bmi) + " kg/m^2";
	}

	/**
	 * Replaces commas with dot, and removes all invalid character.
	 */
	private static String stringValidator(String value) {
		return value.replace(",", ".").toLowerCase().replaceAll("[^a-z&&[^0-9&&[^.]]]", "");
	}

	/**
	 * Get a raw data from user which contain value and unit, this method split this
	 * values, and convert this values to metric values
	 * 
	 * @return metric value
	 */
	private static double getValue(String rawValue, Map<String, Double> convert) {
		String unit = ""; // store the unit
		int firstCharIndex; // store the index where unit starts

		for (Iterator<Entry<String, Double>> it = convert.entrySet().iterator(); it.hasNext();) {
			Entry<String, Double> actual = it.next();
			firstCharIndex = getFirstCharIndex(rawValue.toLowerCase());
			if (firstCharIndex != rawValue.length() - 1)
				unit = rawValue.substring(firstCharIndex).toLowerCase();

			if (unit.equals(""))
				unit = convert == convertToKilograms ? "kg" : "m";

			if (unit.equals(actual.getKey())) {

				return actual.getValue() * Double.parseDouble(rawValue.substring(0, firstCharIndex).trim());
			}
		}
		throw new IllegalArgumentException("uninterpretable paramteres");
	}

	/**
	 * evaluates the BMI in which interval it falls
	 */
	private static String getResults(double bmi) {
		for (Iterator<Entry<String, Range>> it = bmiTable.entrySet().iterator(); it.hasNext();) {
			Entry<String, Range> actual = it.next();
			if (actual.getValue().contains(bmi))
				return actual.getKey();
		}
		return null;
	}

	/**
	 * search for the first index which is a letter
	 */
	private static int getFirstCharIndex(String text) {
		int i = 0;
		for (char ch : text.toCharArray()) {
			if (ch > 96 && ch < 123)
				return i;
			i++;
		}
		return i;

	}

}
