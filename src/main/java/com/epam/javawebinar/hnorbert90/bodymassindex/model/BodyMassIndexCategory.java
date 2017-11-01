package com.epam.javawebinar.hnorbert90.bodymassindex.model;

public class BodyMassIndexCategory {

    private final double min;
    private final double max;
    private final String category;

    public BodyMassIndexCategory(String category, double min, double max) {
        this.min = min;
        this.max = max;
        this.category = category;
    }

    @Override
    public String toString() {
        return category+" BMI range: " + min + "kg/m2 - " + max + "kg/m2";
    }

    public boolean contains(double value) {
        return (value >= min && value < max);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String getCategory() {
        return category;
    }

}
