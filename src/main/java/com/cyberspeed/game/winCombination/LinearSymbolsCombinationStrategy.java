package com.cyberspeed.game.winCombination;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.symbol.StandardSymbol;

import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class LinearSymbolsCombinationStrategy implements WinCombinationStrategy {

    private final Cell[][] coveredAreas;

    public LinearSymbolsCombinationStrategy(Cell[][] coveredAreas) {
        this.coveredAreas = requireNonNull(coveredAreas);
    }

    @Override
    public boolean isApplicable(StandardSymbol standardSymbol, String[][] matrix) {
        String symbolName = standardSymbol.getName();

        boolean combinationApplied;
        for (int i = 0; i < coveredAreas.length; i++) {
            combinationApplied = true;
            for (int j = 0; j < coveredAreas[i].length; j++) {
                Cell cell = coveredAreas[i][j];
                if (!symbolName.equalsIgnoreCase(matrix[cell.rowIndex][cell.columnIndex])) {
                    combinationApplied = false;
                    break;
                }
            }
            if (combinationApplied) return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearSymbolsCombinationStrategy that = (LinearSymbolsCombinationStrategy) o;
        return Objects.deepEquals(coveredAreas, that.coveredAreas);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(coveredAreas);
    }

    @Override
    public String toString() {
        return "LinearSymbolsCombinationStrategy{" +
                "coveredAreas=" + Arrays.toString(coveredAreas) +
                '}';
    }
}
