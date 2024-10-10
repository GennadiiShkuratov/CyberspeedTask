package com.cyberspeed.game.symbol;

import java.math.BigDecimal;

import static com.cyberspeed.game.Utils.round;
import static java.util.Objects.requireNonNull;

public record StandardSymbol(String name, BigDecimal multiplier) implements Symbol {

    public StandardSymbol(String name, BigDecimal multiplier) {
        this.name = requireNonNull(name).toUpperCase();
        this.multiplier = round(multiplier);
    }

}
