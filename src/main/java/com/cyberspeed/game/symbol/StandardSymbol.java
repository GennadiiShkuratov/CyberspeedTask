package com.cyberspeed.game.symbol;

import com.cyberspeed.game.Utils;

import java.math.BigDecimal;
import java.util.Objects;

import static com.cyberspeed.game.Utils.round;
import static java.util.Objects.requireNonNull;

public class StandardSymbol implements Symbol {

    private final String name;
    private BigDecimal multiplier;

    public StandardSymbol(String name, BigDecimal multiplier) {
        this.name = requireNonNull(name).toUpperCase();
        this.multiplier = round(multiplier);
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardSymbol that = (StandardSymbol) o;
        return Objects.equals(name, that.name) && Objects.equals(multiplier, that.multiplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, multiplier);
    }

    @Override
    public String toString() {
        return "StandardSymbol{" +
                "name='" + name + '\'' +
                ", multiplier=" + multiplier +
                '}';
    }
}
