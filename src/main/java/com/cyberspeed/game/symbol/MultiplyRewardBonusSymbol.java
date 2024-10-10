package com.cyberspeed.game.symbol;

import com.cyberspeed.game.bonus.Bonus;

import java.math.BigDecimal;
import java.util.Objects;

import static com.cyberspeed.game.Utils.round;
import static com.cyberspeed.game.bonus.GameImpact.INCREASE_REWARD;
import static com.cyberspeed.game.bonus.GameImpact.MISS_GAME;
import static java.util.Objects.requireNonNull;

public class MultiplyRewardBonusSymbol implements BonusSymbol {

    private final String name;
    private final BigDecimal multiplier;

    public MultiplyRewardBonusSymbol(String name, BigDecimal multiplier) {
        this.name = requireNonNull(name).toUpperCase();

        if (multiplier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("multiplier can't be negative or Zero");
        }

        this.multiplier = round(multiplier);
    }

    @Override
    public Bonus applyTo(Bonus bonus) {
        return new Bonus(
                this.multiplier.multiply(bonus.getRewardMultiplier()),
                bonus.getExtraReward(),
                missGame() || bonus.missGameStatus() ? MISS_GAME : INCREASE_REWARD);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiplyRewardBonusSymbol that = (MultiplyRewardBonusSymbol) o;
        return Objects.equals(name, that.name) && Objects.equals(multiplier, that.multiplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, multiplier);
    }

    @Override
    public String toString() {
        return "MultiplyRewardBonus{" +
                "name='" + name + '\'' +
                ", multiplier=" + multiplier +
                '}';
    }
}
