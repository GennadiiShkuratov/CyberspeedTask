package com.cyberspeed.game.matrix;

import com.cyberspeed.game.probability.SymbolProbability;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class DefaultMatrixGenerator implements MatrixGenerator {

    private final int columns;
    private final int rows;

    final Map<Cell, TreeMap<Long, String>> symbolProbabilityWeightByCell;

    private DefaultMatrixGenerator(int columns,
                                   int rows,
                                   Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell,
                                   List<SymbolProbability> symbolProbabilitiesAcrossMatrix) {
        if(columns <= 0) throw new IllegalArgumentException("Columns number must be bigger then Zero");
        if(rows <= 0) throw new IllegalArgumentException("Rows number must be bigger then Zero");

        this.columns = columns;
        this.rows = rows;

        validate(columns, rows, symbolProbabilitiesByCell, symbolProbabilitiesAcrossMatrix);
        symbolProbabilityWeightByCell = buildSymbolProbabilityWeightByCellDetails(columns, rows, symbolProbabilitiesByCell, symbolProbabilitiesAcrossMatrix);

    }

    private void validate(int columns, int rows, Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell, List<SymbolProbability> symbolProbabilitiesAcrossMatrix) {
        requireNonNull(symbolProbabilitiesByCell);
        if(symbolProbabilitiesByCell.isEmpty()) {
            throw new IllegalArgumentException("symbolProbabilitiesByCell can't be empty");
        }


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<SymbolProbability> probabilities = symbolProbabilitiesByCell.get(new Cell(i, j));
                if (Objects.isNull(probabilities) || probabilities.isEmpty()) {
                    throw new IllegalArgumentException(String.format("There are missed standard symbol probability configuration for cell %d:%d", i, j));
                }
            }
        }

        requireNonNull(symbolProbabilitiesAcrossMatrix);
        if(symbolProbabilitiesAcrossMatrix.isEmpty()) {
            throw new IllegalArgumentException("symbolProbabilitiesAcrossMatrix can't be empty");
        }
    }

    private Map<Cell, TreeMap<Long, String>> buildSymbolProbabilityWeightByCellDetails(int columns, int rows, Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell, List<SymbolProbability> symbolProbabilitiesAcrossMatrix) {
        Map<Cell, TreeMap<Long, String>> symbolProbabilityAcrossByCell = new HashMap<>();

        for (Map.Entry<Cell, List<SymbolProbability>> symbolProbabilitiesByCellEntry : symbolProbabilitiesByCell.entrySet()) {
            TreeMap<Long, String> symbolByProbability = new TreeMap<>();

            long index = 0;
            for (SymbolProbability symbolProbability : symbolProbabilitiesAcrossMatrix){
                int probability = symbolProbability.getProbability();
                index += probability;
                symbolByProbability.put(index, symbolProbability.getSymbolName());
            }

            for (SymbolProbability symbolProbability : symbolProbabilitiesByCellEntry.getValue()){
                int probability = symbolProbability.getProbability() * rows * columns;
                index += probability;
                symbolByProbability.put(index, symbolProbability.getSymbolName());
            }

            symbolProbabilityAcrossByCell.put(symbolProbabilitiesByCellEntry.getKey(), symbolByProbability);
        }

        return Collections.unmodifiableMap(symbolProbabilityAcrossByCell);
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[rows][columns];
        SplittableRandom random = new SplittableRandom();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                TreeMap<Long, String> symbolsByProbability = symbolProbabilityWeightByCell.get(new Cell(row, column));

                long probabilityIndex = random.nextLong(symbolsByProbability.lastKey());
                matrix[row][column] = symbolsByProbability.ceilingEntry(probabilityIndex).getValue();
            }
        }

        return matrix;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int columns;
        private int rows;
        private Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell;
        private List<SymbolProbability> symbolProbabilitiesAcrossMatrix;

        public Builder setColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder setSymbolProbabilitiesByCell(Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell) {
            this.symbolProbabilitiesByCell = symbolProbabilitiesByCell;
            return this;
        }

        public Builder setSymbolProbabilitiesAcrossMatrix(List<SymbolProbability> symbolProbabilitiesAcrossMatrix) {
            this.symbolProbabilitiesAcrossMatrix = symbolProbabilitiesAcrossMatrix;
            return this;
        }

        public DefaultMatrixGenerator build() {
            return new DefaultMatrixGenerator(columns, rows, symbolProbabilitiesByCell, symbolProbabilitiesAcrossMatrix);
        }
    }

   
}
