package com.cyberspeed.game.winCombination;

import com.cyberspeed.game.symbol.StandardSymbol;

public interface WinCombinationStrategy {

    boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix);
}
