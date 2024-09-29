package com.cyberspeed.game.bonus;

import com.cyberspeed.game.symbol.BonusSymbol;

import java.math.BigDecimal;
import java.util.List;

import static com.cyberspeed.game.Utils.round;
import static java.util.Objects.requireNonNull;

public class Bonus {

    private final BigDecimal rewardMultiplier;
    private final BigDecimal extraReward;
    private final GameImpact gameImpact;

    public Bonus(BigDecimal rewardMultiplier, BigDecimal extraAmount) {
        this.rewardMultiplier = round(requireNonNull(rewardMultiplier));
        this.extraReward = round(requireNonNull(extraAmount));
        this.gameImpact = GameImpact.INCREASE_REWARD;
    }

    public Bonus(BigDecimal rewardMultiplier, BigDecimal extraReward, GameImpact gameImpact) {
        this.rewardMultiplier = round(requireNonNull(rewardMultiplier));
        this.extraReward = round(requireNonNull(extraReward));
        this.gameImpact = gameImpact;
    }

    public BigDecimal applyToReward(BigDecimal reward){
        if (reward.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return reward.multiply(rewardMultiplier).add(extraReward);
    }

    public static Bonus generateFrom(List<BonusSymbol> bonusSymbols) {
        Bonus bonus = emptyBonus();
        for (BonusSymbol bonusSymbol : bonusSymbols) {
            if (bonusSymbol.missGame()) {
                return new Bonus(BigDecimal.ZERO, BigDecimal.ZERO, GameImpact.MISS_GAME);
            }

            bonus = bonusSymbol.applyTo(bonus);
        }

        return bonus;
    }

    public boolean missGameStatus() {
        return gameImpact == GameImpact.MISS_GAME;
    }

    public static Bonus emptyBonus() {
        return new Bonus(new BigDecimal(1), BigDecimal.ZERO, GameImpact.INCREASE_REWARD);
    }

    public BigDecimal getRewardMultiplier() {
        return rewardMultiplier;
    }

    public BigDecimal getExtraReward() {
        return extraReward;
    }
}
