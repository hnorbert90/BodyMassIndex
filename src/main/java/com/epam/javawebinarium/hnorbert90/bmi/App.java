package com.epam.javawebinarium.hnorbert90.bmi;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println(BMI.calculateBMI(1.85,80));
        System.out.println(BMI.calculateBMI(1.85D, 80F));
        System.out.println(BMI.calculateBMI(1.85, 3D));
        System.out.println(BMI.calculateBMI("185cm", "80kg"));
        System.out.println(BMI.calculateBMI("1?85?c$$m", "8[}0k?g"));
        System.out.println(BMI.calculateBMI("185,0 cm", "80.0 kg"));
        System.out.println(BMI.calculateBMI("1,85m", "800dkg"));
        System.out.println(BMI.calculateBMI("185feet", "80pound"));
        System.out.println(BMI.calculateBMI("185milimeter", "80g"));
        System.out.println(BMI.calculateBMI("185millimeters", "80kilograms"));
        System.out.println(BMI.calculateBMI("185millimeters", "80dekagram"));
        System.out.println(BMI.calculateBMI("185millimeters", "80"));
        System.out.println(BMI.calculateBMI("185", "80dekagram"));
    }
}
