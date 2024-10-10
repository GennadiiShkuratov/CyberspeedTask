package com.cyberspeed.game.wincombination;

import com.cyberspeed.game.NamedObject;
import com.cyberspeed.game.symbol.StandardSymbol;

import java.math.BigDecimal;
import java.util.Objects;

import static com.cyberspeed.game.Utils.round;
import static java.util.Objects.requireNonNull;

public class WinCombination implements NamedObject {

    private final String name;
    private final String when;
    private final String group;
    private final BigDecimal multiplier;
    private final WinCombinationStrategy strategy;

    public WinCombination(String name, String when, BigDecimal multiplier, String group, WinCombinationStrategy strategy) {
        this.name = requireNonNull(name);
        this.when = requireNonNull(when);
        this.group = requireNonNull(group);
        this.multiplier = round(requireNonNull(multiplier));
        this.strategy = requireNonNull(strategy);
    }

    public boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix){
        return strategy.isApplicable(standardSymbol, matrix);
    }

    public String name() {
        return name;
    }

    public String getWhen() {
        return when;
    }

    public String getGroup() {
        return group;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WinCombination that = (WinCombination) o;
        return Objects.equals(name, that.name) && Objects.equals(when, that.when) && Objects.equals(group, that.group) && Objects.equals(multiplier, that.multiplier) && Objects.equals(strategy, that.strategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, when, group, multiplier, strategy);
    }

    @Override
    public String toString() {
        return "WinCombination{" +
                "name='" + name + '\'' +
                ", when='" + when + '\'' +
                ", group='" + group + '\'' +
                ", multiplier=" + multiplier +
                ", strategy=" + strategy +
                '}';
    }
}
