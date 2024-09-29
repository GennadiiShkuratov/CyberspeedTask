package com.cyberspeed.game.winCombination;

import com.cyberspeed.game.symbol.StandardSymbol;

import java.util.Objects;

public class SameSymbolsCombinationStrategy implements WinCombinationStrategy {

    private int count;

    public SameSymbolsCombinationStrategy(int count) {
        this.count = count;
    }

    @Override
    public boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix) {
        int symbolsCount = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (standardSymbol.getName().equalsIgnoreCase(matrix[i][j])) symbolsCount++;
                if (symbolsCount >= count) return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SameSymbolsCombinationStrategy that = (SameSymbolsCombinationStrategy) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(count);
    }

    @Override
    public String toString() {
        return "SameSymbolsCombinationStrategy{" +
                "count=" + count +
                '}';
    }
}
