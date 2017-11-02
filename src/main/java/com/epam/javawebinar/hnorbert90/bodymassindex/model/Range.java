package com.epam.javawebinar.hnorbert90.bodymassindex.model;

/**
 * A Range represents an interval—a set of values with a beginning and an end.
 */
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

    /**
     * @param value (double)
     * @return <b>true</b> if the value is in the interval
     */
    public boolean contains(double value) {
        return (value >= min && value < max);
    }

    @Override
    public String toString() {
        return "Range [min=" + min + ", max=" + max + "]";
    }
}
