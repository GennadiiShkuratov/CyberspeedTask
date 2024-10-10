package com.cyberspeed.game;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.symbol.*;
import com.cyberspeed.game.wincombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.WinCombination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RewardTest {

    @Test
    void bettingAmountIsMandatory() {
        Reward.Builder builder1 = Reward.builder()
                .setMatrix(new String[][]{
                        new String[]{"A", "A", "B"},
                        new String[]{"A", "C", "B"},
                        new String[]{"A", "A", "B"},
                })
                .setBettingAmount(null)
                .setBonusSymbols(Collections.emptyList())
                .setWinCombinations(Collections.emptyMap());

        assertThrows(NullPointerException.class,
                builder1::build
        );

        Reward.Builder builder2 = Reward.builder()
                .setMatrix(new String[][]{
                        new String[]{"A", "A", "B"},
                        new String[]{"A", "C", "B"},
                        new String[]{"A", "A", "B"},
                })
                .setBettingAmount(BigDecimal.ZERO)
                .setBonusSymbols(Collections.emptyList())
                .setWinCombinations(Collections.emptyMap());
        assertThrows(IllegalArgumentException.class,
                builder2::build
        );

        Reward.Builder builder3 = Reward.builder()
                .setMatrix(new String[][]{
                        new String[]{"A", "A", "B"},
                        new String[]{"A", "C", "B"},
                        new String[]{"A", "A", "B"},
                })
                .setBettingAmount(new BigDecimal(100).negate())
                .setBonusSymbols(Collections.emptyList())
                .setWinCombinations(Collections.emptyMap());

        assertThrows(IllegalArgumentException.class,
                builder3::build);
    }

    @Test
    void nonEmptyMatrixIsMandatory(){
        Reward.Builder builder = Reward.builder()
                .setMatrix(new String[0][0])
                .setBettingAmount(ONE)
                .setBonusSymbols(Collections.emptyList())
                .setWinCombinations(Collections.emptyMap());

        assertThrows(IllegalArgumentException.class,
                builder::build);
    }

    @Test
    void calculateTotalRewardConsideringAllWinningCombinations(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "B"},
                new String[]{"A", "C", "B"},
                new String[]{"A", "A", "B"},
        };

        List<BonusSymbol> bonusSymbols = new ArrayList<>();

        BigDecimal bettingAmount = BigDecimal.valueOf(1);

        Map<StandardSymbol, List<WinCombination>> winCombinations = new HashMap<>();
        winCombinations.put(new StandardSymbol("A", BigDecimal.valueOf(10)), new ArrayList<>(){{
            add(new WinCombination("same_symbol_5_times", "same_symbols", BigDecimal.valueOf(3), "same_symbols", new SameSymbolsCombinationStrategy((5))));
            add(new WinCombination("same_symbols_vertically", "linear_symbols", BigDecimal.valueOf(10), "vertically_linear_symbols",
                    new LinearSymbolsCombinationStrategy(new Cell[][]{
                            new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                            new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                            new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                    })));
        }});
        winCombinations.put(new StandardSymbol("B", BigDecimal.valueOf(5)), new ArrayList<>(){{
            add(new WinCombination("same_symbol_3_times", "", BigDecimal.valueOf(2), "", new SameSymbolsCombinationStrategy((3))));
            add(new WinCombination("same_symbols_vertically", "linear_symbols", BigDecimal.valueOf(10), "vertically_linear_symbols",
                    new LinearSymbolsCombinationStrategy(new Cell[][]{
                            new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                            new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                            new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                    })));
        }});

        Reward reward = Reward.builder()
                .setMatrix(matrix)
                .setBettingAmount(bettingAmount)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .build();

        //When
        BigDecimal value = reward.calculateTotalReward();

        //Then
        Assertions.assertEquals(new BigDecimal(400), value);

    }

    @Test
    void calculateTotalRewardConsideringAllMultiplierBonuses(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "x10"},
                new String[]{"x2", "x100", "A"},
                new String[]{"A", "x1000", "A"},
        };
        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        bonusSymbols.add(new MultiplyRewardBonusSymbol("x1000", new BigDecimal(1000)));
        bonusSymbols.add(new MultiplyRewardBonusSymbol("x100", new BigDecimal(100)));
        bonusSymbols.add(new MultiplyRewardBonusSymbol("x10", new BigDecimal(10)));
        bonusSymbols.add(new MultiplyRewardBonusSymbol("x2", new BigDecimal(2)));

        BigDecimal bettingAmount = BigDecimal.valueOf(1);

        Map<StandardSymbol, List<WinCombination>> winCombinations = new HashMap<>();
        winCombinations.put(new StandardSymbol("A", ONE), new ArrayList<>(){{
            add(new WinCombination("same_symbol_5_times", "same_symbols", ONE, "same_symbols", new SameSymbolsCombinationStrategy((5))));
        }});

        Reward reward = Reward.builder()
                .setMatrix(matrix)
                .setBettingAmount(bettingAmount)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .build();

        //When
        BigDecimal value = reward.calculateTotalReward();

        //Then
        Assertions.assertEquals(BigDecimal.valueOf(2000000), value);

    }

    @Test
    void calculateTotalRewardConsideringAllExtraBonuses(){
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "+5000"},
                new String[]{"+5", "+50", "A"},
                new String[]{"A", "+500", "A"},
        };

        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        bonusSymbols.add(new ExtraBonusSymbol("+5000", new BigDecimal(5000)));
        bonusSymbols.add(new ExtraBonusSymbol("+500", new BigDecimal(500)));
        bonusSymbols.add(new ExtraBonusSymbol("+50", new BigDecimal(50)));
        bonusSymbols.add(new ExtraBonusSymbol("+5", new BigDecimal(5)));

        BigDecimal bettingAmount = BigDecimal.valueOf(1);

        Map<StandardSymbol, List<WinCombination>> winCombinations = new HashMap<>();
        winCombinations.put(new StandardSymbol("A", ONE), new ArrayList<>(){{
            add(new WinCombination("same_symbol_5_times", "same_symbols", ONE, "same_symbols", new SameSymbolsCombinationStrategy((5))));
        }});

        Reward reward = Reward.builder()
                .setMatrix(matrix)
                .setBettingAmount(bettingAmount)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .build();

        //When
        BigDecimal value = reward.calculateTotalReward();

        //Then
        Assertions.assertEquals(BigDecimal.valueOf(5556), value);

    }


    @Test
    void calculateTotalShouldBeZeroWhenThereIsMissBonusSymbol() {
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "B"},
                new String[]{"A", "+1000", "B"},
                new String[]{"A", "MISS", "B"},
        };

        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        bonusSymbols.add(new ExtraBonusSymbol("+1000", new BigDecimal(1000)));
        bonusSymbols.add(new MissRewardBonusSymbol());

        BigDecimal bettingAmount = BigDecimal.valueOf(100);

        Map<StandardSymbol, List<WinCombination>> winCombinations = new HashMap<>();
        winCombinations.put(new StandardSymbol("A", ONE), new ArrayList<>(){{
            add(new WinCombination("same_symbol_5_times", "same_symbols", ONE, "same_symbols", new SameSymbolsCombinationStrategy((5))));
            add(new WinCombination("same_symbols_vertically", "linear_symbols", ONE, "vertically_linear_symbols",
                    new LinearSymbolsCombinationStrategy(new Cell[][]{
                            new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                            new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                            new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                    })));
        }});
        winCombinations.put(new StandardSymbol("B", ONE), new ArrayList<>(){{
            add(new WinCombination("same_symbol_3_times", "", ONE, "", new SameSymbolsCombinationStrategy((3))));
            add(new WinCombination("same_symbols_vertically", "linear_symbols", ONE, "vertically_linear_symbols",
                    new LinearSymbolsCombinationStrategy(new Cell[][]{
                            new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                            new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                            new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                    })));
        }});

        Reward reward = Reward.builder()
                .setMatrix(matrix)
                .setBettingAmount(bettingAmount)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .build();

        //When
        BigDecimal value = reward.calculateTotalReward();

        //Then
        Assertions.assertEquals(BigDecimal.ZERO, value);
    }


}