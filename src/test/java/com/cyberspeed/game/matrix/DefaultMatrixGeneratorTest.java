package com.cyberspeed.game.matrix;

import com.cyberspeed.game.probability.SymbolProbability;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.random.RandomGenerator;
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
                                            .map(v -> new SymbolProbability(v.symbolName(), v.probability() * columns * rows)),
                            symbolProbabilitiesAcrossMatrix.stream()
                    )
                    .sorted(Comparator.comparing(SymbolProbability::probability))
                    .toList();

            List<String> symbolsOrderedByWeight = new ArrayList<>(symbolProbabilityWeightByCell.get(cell).values());

            Assertions.assertEquals(sortedSymbolProbabilitiesByProbability.size(), symbolsOrderedByWeight.size());
            for (int i = 0; i < sortedSymbolProbabilitiesByProbability.size(); i++) {
                Assertions.assertEquals(sortedSymbolProbabilitiesByProbability.get(i).symbolName(), symbolsOrderedByWeight.get(i));
            }

        }
    }

    @Test
    void probabilityOfSymbolShouldReflectedInSymbolWeight(){
        //Given
        RandomGenerator randomGenerator = new RandomGenerator() {
            int counter;
            int[] weight = new int[]{0, 1, 2, 3, 20, 21, 65, 66, 155};

            @Override
            public long nextLong() {
                return 0;
            }

            @Override
            public long nextLong(long bound ) {
                return weight[counter++];
            }
        };

        List<SymbolProbability> symbolProbabilities = new ArrayList<>();
        symbolProbabilities.add(new SymbolProbability("A", 2));//21
        symbolProbabilities.add(new SymbolProbability("B", 5));//66
        symbolProbabilities.add(new SymbolProbability("C", 10));//156

        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = new HashMap<>();
        symbolProbabilitiesByCell.put(new Cell(0, 0), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(0, 1), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(0, 2), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(1, 0), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(1, 1), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(1, 2), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(2, 0), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(2, 1), symbolProbabilities);
        symbolProbabilitiesByCell.put(new Cell(2, 2), symbolProbabilities);

        List<SymbolProbability> symbolProbabilitiesAcrossMatrix = new ArrayList<>();
        symbolProbabilitiesAcrossMatrix.add(new SymbolProbability("10x", 1));//1
        symbolProbabilitiesAcrossMatrix.add(new SymbolProbability("5x", 2));//3

        int rows = 3;
        int columns = 3;

        DefaultMatrixGenerator matrixGenerator = DefaultMatrixGenerator.builder()
                .setRows(rows)
                .setColumns(columns)
                .setSymbolProbabilitiesAcrossMatrix(symbolProbabilitiesAcrossMatrix)
                .setSymbolProbabilitiesByCell(symbolProbabilitiesByCell)
                .setRandomGenerator(randomGenerator)
                .build();

        //When
        String[][] matrix = matrixGenerator.generateMatrix();

        //Then
        System.out.println(Arrays.deepToString(matrix));

       Assertions.assertEquals("10X", matrix[0][0]);
       Assertions.assertEquals("5X", matrix[0][1]);
       Assertions.assertEquals("5X", matrix[0][2]);

       Assertions.assertEquals("A", matrix[1][0]);
       Assertions.assertEquals("A", matrix[1][1]);
       Assertions.assertEquals("B", matrix[1][2]);

       Assertions.assertEquals("B", matrix[2][0]);
       Assertions.assertEquals("C", matrix[2][1]);
       Assertions.assertEquals("C", matrix[2][2]);

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