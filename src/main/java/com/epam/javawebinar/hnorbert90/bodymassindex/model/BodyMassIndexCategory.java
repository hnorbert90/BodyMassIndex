package com.epam.javawebinar.hnorbert90.bodymassindex.model;

public class BodyMassIndexCategory extends Range{

    private final String category;
    /**
     * Class for store BMI categories
     * @param category
     * @param min
     * @param max
     */
    public BodyMassIndexCategory(String category, double min, double max) {
        super(min,max);
        this.category = category;
    }

    @Override
    public String toString() {
        return category+" BMI range: " + getMin() + "kg/m2 - " + getMax() + "kg/m2";
    }

    public String getCategory() {
        return category;
    }

}
