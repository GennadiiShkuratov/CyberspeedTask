package com.cyberspeed.game.symbol;

import com.cyberspeed.game.bonus.Bonus;

public class MissRewardBonusSymbol implements BonusSymbol {

    public MissRewardBonusSymbol() {}

    @Override
    public Bonus applyTo(Bonus bonus) {
        return Bonus.emptyBonus();
    }

    @Override
    public boolean missGame() {
        return true;
    }


    @Override
    public String getName() {
        return "MISS";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "MissRewardBonus{}";
    }
}
