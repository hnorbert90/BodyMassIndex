package com.epam.javawebinarium.hnorbert90.bmi;

public class Range {
	private double min;
	private double max;

	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public boolean contains(double value) {
		return (value >= min && value < max);
	}
}