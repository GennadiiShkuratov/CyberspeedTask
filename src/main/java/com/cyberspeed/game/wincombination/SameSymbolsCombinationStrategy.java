package com.cyberspeed.game.wincombination;

import com.cyberspeed.game.symbol.StandardSymbol;

import java.util.Objects;

public class SameSymbolsCombinationStrategy implements WinCombinationStrategy {

    private final int count;

    public SameSymbolsCombinationStrategy(int count) {
        this.count = count;
    }

    @Override
    public boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix) {
        int symbolsCount = 0;
        for (String[] matrixRow : matrix) {
            for (int j = 0; j < matrixRow.length; j++) {
                if (standardSymbol.name().equalsIgnoreCase(matrixRow[j])) symbolsCount++;
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
