package com.sunland.jwyxy.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalFormatUtils {

    public static String format2(float value) {
        DecimalFormat df = new DecimalFormat("00.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
}
