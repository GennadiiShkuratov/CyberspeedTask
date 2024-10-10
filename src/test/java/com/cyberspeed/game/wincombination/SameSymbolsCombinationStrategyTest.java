package com.cyberspeed.game.wincombination;

import com.cyberspeed.game.symbol.StandardSymbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SameSymbolsCombinationStrategyTest {

    @Test
    void applySameSymbolsStrategyIfSymbolsCountCorrespondToCombination(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "B", "A"},
                new String[]{"b", "B", "X5"},
                new String[]{"A", "+1000", "A"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);


        //When
        WinCombinationStrategy threeTimesSameSymbolsCombinationStrategy = new SameSymbolsCombinationStrategy(3);
        WinCombinationStrategy fourTimesSameSymbolsCombinationStrategy = new SameSymbolsCombinationStrategy(4);

        //Then
        Assertions.assertTrue(threeTimesSameSymbolsCombinationStrategy.isApplicable(a, matrix));
        Assertions.assertTrue(threeTimesSameSymbolsCombinationStrategy.isApplicable(b, matrix));
        Assertions.assertTrue(fourTimesSameSymbolsCombinationStrategy.isApplicable(a, matrix));

    }

    @Test
    void strategyIsNotApplicableWhenSymbolsCountLessThenExpected(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "B", "A"},
                new String[]{"C", "B", "X5"},
                new String[]{"A", "+1000", "C"},
        };
        StandardSymbol a = new StandardSymbol("A", BigDecimal.ONE);
        StandardSymbol b = new StandardSymbol("B", BigDecimal.ONE);


        //When
        WinCombinationStrategy threeTimesSameSymbolsCombinationStrategy = new SameSymbolsCombinationStrategy(3);
        WinCombinationStrategy fourTimesSameSymbolsCombinationStrategy = new SameSymbolsCombinationStrategy(4);

        //Then
        Assertions.assertFalse(threeTimesSameSymbolsCombinationStrategy.isApplicable(b, matrix));
        Assertions.assertFalse(fourTimesSameSymbolsCombinationStrategy.isApplicable(b, matrix));
        Assertions.assertFalse(fourTimesSameSymbolsCombinationStrategy.isApplicable(a, matrix));

    }


}