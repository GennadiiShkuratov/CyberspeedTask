package com.cyberspeed.game.symbol;

import com.cyberspeed.game.bonus.Bonus;

public class MissRewardBonusSymbol implements BonusSymbol {

    @Override
    public Bonus applyTo(Bonus bonus) {
        return Bonus.emptyBonus();
    }

    @Override
    public boolean missGame() {
        return true;
    }


    @Override
    public String name() {
        return "MISS";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj != null && getClass() == obj.getClass();
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
