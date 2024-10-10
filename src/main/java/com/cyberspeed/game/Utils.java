package com.cyberspeed.game;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    private Utils(){}

    public static BigDecimal round(BigDecimal value){
        return value.setScale(2, RoundingMode.DOWN);
    }

    public static BigDecimal roundToWholeNumber(BigDecimal value){
        return value.setScale(0, RoundingMode.DOWN);
    }

}
