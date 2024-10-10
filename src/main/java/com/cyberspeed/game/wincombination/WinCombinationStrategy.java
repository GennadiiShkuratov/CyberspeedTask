package com.cyberspeed.game.wincombination;

import com.cyberspeed.game.symbol.StandardSymbol;

public interface WinCombinationStrategy {

    boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix);
}
