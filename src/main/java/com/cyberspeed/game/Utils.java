package com.cyberspeed.game;

import java.math.BigDecimal;

public class Utils {

    public static BigDecimal round(BigDecimal value){
        return value.setScale(2, BigDecimal.ROUND_FLOOR);
    }

}
