package com.epam.javawebinar.hnorbert90.bodymassindex.model;

public class Range {
	private double min;
	private double max;

	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	
    public double getMin() {
        return min;
    }

    
    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Range [min=" + min + ", max=" + max + "]";
    }

    public boolean contains(double value) {
		return (value >= min && value < max);
	}
}