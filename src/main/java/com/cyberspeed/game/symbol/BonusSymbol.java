package com.cyberspeed.game.symbol;


import com.cyberspeed.game.bonus.Bonus;

public interface BonusSymbol extends Symbol {

    Bonus applyTo(Bonus bonus);

    default boolean missGame(){
        return false;
    }



}
