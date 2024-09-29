package com.cyberspeed.game.symbol;

import com.cyberspeed.game.bonus.Bonus;

import java.math.BigDecimal;
import java.util.Objects;

import static com.cyberspeed.game.bonus.GameImpact.MISS_GAME;
import static com.cyberspeed.game.bonus.GameImpact.INCREASE_REWARD;
import static com.cyberspeed.game.Utils.round;
import static java.math.BigDecimal.ZERO;
import static java.util.Objects.requireNonNull;

public class ExtraBonusSymbol implements BonusSymbol {

    private final String name;
    private BigDecimal value;

    public ExtraBonusSymbol(String name, BigDecimal value) {
        this.name = requireNonNull(name).toUpperCase();

        requireNonNull(value);
        if (value.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException("value can't be negative or Zero");
        }

        this.value = round(value);
    }

    @Override
    public Bonus applyTo(Bonus bonus) {
        return new Bonus(
                bonus.getRewardMultiplier(),
                this.value.add(bonus.getExtraReward()),
                missGame() || bonus.missGameStatus() ? MISS_GAME : INCREASE_REWARD);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraBonusSymbol that = (ExtraBonusSymbol) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "ExtraBonus{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
