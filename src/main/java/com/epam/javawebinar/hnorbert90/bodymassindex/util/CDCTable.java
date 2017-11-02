package com.epam.javawebinar.hnorbert90.bodymassindex.util;

/**
 * CDCTable stores index values of the "CDC_DATA.txt" CSV table columns.
 */
public enum CDCTable {
    GENDER(0), AGE(1), BMI_3(2), BMI_5(3), BMI_10(4), BMI_25(5), BMI_50(6), BMI_75(7), BMI_85(
            8), BMI_90(9), BMI_95(10), BMI_97(11);

    private final int index;

    CDCTable(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
