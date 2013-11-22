package com.iKairos.utils;

import java.text.DecimalFormat;

public class Numbers {

    public static double round(double number, int decimalPlaces) {

        String formatConstructorString = "#.";

        for (int p = 0; p < decimalPlaces; p++) {
            formatConstructorString += "#";
        }

        DecimalFormat format = new DecimalFormat(formatConstructorString);
        return Double.parseDouble(format.format(number));
    }

}
