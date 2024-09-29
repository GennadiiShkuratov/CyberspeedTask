package com.cyberspeed.game.winCombination;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.symbol.StandardSymbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class LinearSymbolsCombinationStrategyTest {

    @Test
    public void applyHorizontalLinearSymbolsStrategyIfAtLeastOneSymbolsLineMapped(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "A"},
                new String[]{"B", "B", "B"},
                new String[]{"A", "A", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);


        //When
        Cell[][] coveredAreas = new Cell[][]{
                new Cell[]{new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)},
                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)},
                new Cell[]{new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)}
        };

        WinCombinationStrategy strategy = new LinearSymbolsCombinationStrategy(coveredAreas);

        //Then
        Assertions.assertTrue(strategy.isApplicable(a, matrix));
        Assertions.assertTrue(strategy.isApplicable(b, matrix));

    }


    @Test
    public void strategyIsNotApplicableWhenNoneOfLinesMappedToSpecificSymbol(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "B"},
                new String[]{"A", "+1000", "B"},
                new String[]{"B", "B", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);


        //When
        Cell[][] coveredAreas = new Cell[][]{
                new Cell[]{new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)},
                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)},
                new Cell[]{new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)}
        };

        WinCombinationStrategy strategy = new LinearSymbolsCombinationStrategy(coveredAreas);

        //Then
        Assertions.assertFalse(strategy.isApplicable(a, matrix));
        Assertions.assertFalse(strategy.isApplicable(b, matrix));

    }

    @Test
    public void coveredAreasCanHaveDifferentSizeOfVerticalLines(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "B"},
                new String[]{"A", "B", "b"},
                new String[]{"B", "A", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);

        //When
        Cell[][] coveredAreas = new Cell[][]{
                new Cell[]{new Cell(0, 0), new Cell(1, 0)},
                new Cell[]{new Cell(0, 2), new Cell(1, 2) }
        };

        WinCombinationStrategy strategy = new LinearSymbolsCombinationStrategy(coveredAreas);

        //Then
        Assertions.assertTrue(strategy.isApplicable(a, matrix));
        Assertions.assertTrue(strategy.isApplicable(b, matrix));

    }

    @Test
    public void coveredAreasCanHaveDifferentSizeOfHorizontalLines(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"B", "A", "A"},
                new String[]{"A", "B", "A"},
                new String[]{"B", "B", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);

        //When
        Cell[][] coveredAreas = new Cell[][]{
                new Cell[]{new Cell(0, 1), new Cell(0, 2)},
                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)},
                new Cell[]{new Cell(2, 0), new Cell(2, 1)}
        };

        WinCombinationStrategy strategy = new LinearSymbolsCombinationStrategy(coveredAreas);

        //Then
        Assertions.assertTrue(strategy.isApplicable(a, matrix));
        Assertions.assertTrue(strategy.isApplicable(b, matrix));

    }


    @Test
    public void coveredAreasCanHaveDifferentSizeOfDiagonalLines(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"B", "A", "B"},
                new String[]{"A", "B", "A"},
                new String[]{"A", "A", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);

        //When
        Cell[][] coveredAreas = new Cell[][]{
                new Cell[]{new Cell(1, 1), new Cell(0, 2)},
                new Cell[]{new Cell(0, 0), new Cell(1, 1), new Cell(2, 2)},
                new Cell[]{new Cell(1, 0), new Cell(2, 1)}
        };

        WinCombinationStrategy strategy = new LinearSymbolsCombinationStrategy(coveredAreas);

        //Then
        Assertions.assertTrue(strategy.isApplicable(a, matrix));
        Assertions.assertTrue(strategy.isApplicable(b, matrix));

    }

}