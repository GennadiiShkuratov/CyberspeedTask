package com.cyberspeed.game.matrix;

import com.cyberspeed.game.probability.SymbolProbability;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultMatrixGeneratorTest {

    @Test
    void weightOfSymbolProbabilityShouldReflectItsConfiguration(){
        //Given
        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = getSymbolProbabilitiesByCell();
        List<SymbolProbability> symbolProbabilitiesAcrossMatrix = getSymbolProbabilitiesAcrossMatrix();
        int rows = 4;
        int columns = 5;

        DefaultMatrixGenerator matrixGenerator = DefaultMatrixGenerator.builder()
                .setRows(rows)
                .setColumns(columns)
                .setSymbolProbabilitiesAcrossMatrix(symbolProbabilitiesAcrossMatrix)
                .setSymbolProbabilitiesByCell(symbolProbabilitiesByCell)
                .build();


        //Then
        Map<Cell, TreeMap<Long, String>> symbolProbabilityWeightByCell = matrixGenerator.symbolProbabilityWeightByCell;
        for (Cell cell : symbolProbabilityWeightByCell.keySet()) {
            List<SymbolProbability> sortedSymbolProbabilitiesByProbability = Stream.concat(
                            symbolProbabilitiesByCell.get(cell).stream()
                                            .map(v -> new SymbolProbability(v.getSymbolName(), v.getProbability() * columns * rows)),
                            symbolProbabilitiesAcrossMatrix.stream()
                    )
                    .sorted(Comparator.comparing(SymbolProbability::getProbability))
                    .toList();

            List<String> symbolsOrderedByWeight = new ArrayList<>(symbolProbabilityWeightByCell.get(cell).values());

            Assertions.assertEquals(sortedSymbolProbabilitiesByProbability.size(), symbolsOrderedByWeight.size());
            for (int i = 0; i < sortedSymbolProbabilitiesByProbability.size(); i++) {
                Assertions.assertEquals(sortedSymbolProbabilitiesByProbability.get(i).getSymbolName(), symbolsOrderedByWeight.get(i));
            }

        }
    }

    @Test
    void missedSymbolProbabilityConfigurationForCell(){
        //Given
        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = getSymbolProbabilitiesByCell().entrySet().stream()
                .filter(v -> !v.getKey().equals(new Cell(1,1)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<SymbolProbability> symbolProbabilitiesAcrossMatrix = getSymbolProbabilitiesAcrossMatrix();
        int rows = 4;
        int columns = 5;

        //Then
        DefaultMatrixGenerator.Builder builder = DefaultMatrixGenerator.builder()
                .setRows(rows)
                .setColumns(columns)
                .setSymbolProbabilitiesAcrossMatrix(symbolProbabilitiesAcrossMatrix)
                .setSymbolProbabilitiesByCell(symbolProbabilitiesByCell);

        Exception exception = assertThrows(IllegalArgumentException.class, builder::build);

        String expectedMessage = "There are missed standard symbol probability configuration for cell 1:1";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }



    private static HashMap<Cell, List<SymbolProbability>> getSymbolProbabilitiesByCell() {
        ArrayList<SymbolProbability> symbolProbabilities = new ArrayList<>();
        symbolProbabilities.add(new SymbolProbability("A", 1));
        symbolProbabilities.add(new SymbolProbability("B", 2));
        symbolProbabilities.add(new SymbolProbability("C", 3));
        symbolProbabilities.add(new SymbolProbability("D", 4));
        symbolProbabilities.add(new SymbolProbability("E", 5));
        symbolProbabilities.add(new SymbolProbability("F", 6));

        return new HashMap<>(){{
            put(new Cell(0, 0), symbolProbabilities);
            put(new Cell(1, 0), symbolProbabilities);
            put(new Cell(2, 0), symbolProbabilities);
            put(new Cell(3, 0), symbolProbabilities);
            put(new Cell(0, 1), symbolProbabilities);
            put(new Cell(1, 1), Arrays.asList(
                    new SymbolProbability("A", 10),
                    new SymbolProbability("B", 20),
                    new SymbolProbability("C", 30),
                    new SymbolProbability("D", 40),
                    new SymbolProbability("E", 50),
                    new SymbolProbability("F", 60)
            ));
            put(new Cell(2, 1), symbolProbabilities);
            put(new Cell(3, 1), symbolProbabilities);
            put(new Cell(0, 2), symbolProbabilities);
            put(new Cell(1, 2), symbolProbabilities);
            put(new Cell(2, 2), symbolProbabilities);
            put(new Cell(3, 2), symbolProbabilities);
            put(new Cell(0, 3), symbolProbabilities);
            put(new Cell(1, 3), symbolProbabilities);
            put(new Cell(2, 3), symbolProbabilities);
            put(new Cell(3, 3), symbolProbabilities);
            put(new Cell(0, 4), symbolProbabilities);
            put(new Cell(1, 4), symbolProbabilities);
            put(new Cell(2, 4), symbolProbabilities);
            put(new Cell(3, 4), symbolProbabilities);
        }};
    }

    private static ArrayList<SymbolProbability> getSymbolProbabilitiesAcrossMatrix() {
        return new ArrayList<>(){{
            add(new SymbolProbability("10x", 1));
            add(new SymbolProbability("5x", 2));
            add(new SymbolProbability("1000", 3));
            add(new SymbolProbability("500", 4));
            add(new SymbolProbability("MISS", 5));
        }};
    }

}