package com.cyberspeed.game;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.probability.SymbolProbability;
import com.cyberspeed.game.symbol.*;
import com.cyberspeed.game.winCombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.WinCombination;

import java.math.BigDecimal;
import java.util.*;

public class PredefinedTestingGameConfigurationDataProvider {

    public static List<WinCombination> getExpectedWinCombinations() {
        List<WinCombination> winCombinations = new ArrayList<>();

        winCombinations.add(
                new WinCombination("same_symbol_3_times",
                        "same_symbols",
                        BigDecimal.valueOf(1),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(3)));
        winCombinations.add(
                new WinCombination("same_symbol_4_times",
                        "same_symbols",
                        BigDecimal.valueOf(1.5),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(4)));
        winCombinations.add(
                new WinCombination("same_symbol_5_times",
                        "same_symbols",
                        BigDecimal.valueOf(2),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(5)));
        winCombinations.add(
                new WinCombination("same_symbol_6_times",
                        "same_symbols",
                        BigDecimal.valueOf(3),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(6)));
        winCombinations.add(
                new WinCombination("same_symbol_7_times",
                        "same_symbols",
                        BigDecimal.valueOf(5),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(7)));
        winCombinations.add(
                new WinCombination("same_symbol_8_times",
                        "same_symbols",
                        BigDecimal.valueOf(10),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(8)));
        winCombinations.add(
                new WinCombination("same_symbol_9_times",
                        "same_symbols",
                        BigDecimal.valueOf(20),
                        "same_symbols",
                        new SameSymbolsCombinationStrategy(9)));

        winCombinations.add(
                new WinCombination("same_symbols_horizontally",
                        "linear_symbols",
                        BigDecimal.valueOf(2),
                        "horizontally_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)},
                                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)},
                                new Cell[]{new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)}
                        })));
        winCombinations.add(
                new WinCombination("same_symbols_vertically",
                        "linear_symbols",
                        BigDecimal.valueOf(2),
                        "vertically_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                                new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                                new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                        })));
        winCombinations.add(
                new WinCombination("same_symbols_diagonally_left_to_right",
                        "linear_symbols",
                        BigDecimal.valueOf(5),
                        "ltr_diagonally_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 0), new Cell(1, 1), new Cell(2, 2)}
                        })));
        winCombinations.add(
                new WinCombination("same_symbols_diagonally_right_to_left",
                        "linear_symbols",
                        BigDecimal.valueOf(5),
                        "rtl_diagonally_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 2), new Cell(1, 1), new Cell(2, 0)}
                        })));


        return winCombinations;
    }

    public static Map<Cell, List<SymbolProbability>> getExpectedSymbolProbabilitiesByCell() {
        List<SymbolProbability> symbolProbabilities = Arrays.asList(
                new SymbolProbability("A", 1),
                new SymbolProbability("B", 2),
                new SymbolProbability("C", 3),
                new SymbolProbability("D", 4),
                new SymbolProbability("E", 5),
                new SymbolProbability("F", 6)
        );

        Map<Cell, List<SymbolProbability>> probabilitiesByCell = new HashMap<>();
        probabilitiesByCell.put(new Cell(0, 0), symbolProbabilities);
        probabilitiesByCell.put(new Cell(0, 1), symbolProbabilities);
        probabilitiesByCell.put(new Cell(0, 2), symbolProbabilities);
        probabilitiesByCell.put(new Cell(1, 0), symbolProbabilities);
        probabilitiesByCell.put(new Cell(1, 1), Arrays.asList(
                new SymbolProbability("A", 10),
                new SymbolProbability("B", 20),
                new SymbolProbability("C", 30),
                new SymbolProbability("D", 40),
                new SymbolProbability("E", 50),
                new SymbolProbability("F", 60)
        ));
        probabilitiesByCell.put(new Cell(1, 2), symbolProbabilities);
        probabilitiesByCell.put(new Cell(2, 0), symbolProbabilities);
        probabilitiesByCell.put(new Cell(2, 1), symbolProbabilities);
        probabilitiesByCell.put(new Cell(2, 2), symbolProbabilities);
        probabilitiesByCell.put(new Cell(3, 0), symbolProbabilities);
        probabilitiesByCell.put(new Cell(3, 1), symbolProbabilities);
        probabilitiesByCell.put(new Cell(3, 2), symbolProbabilities);

        return probabilitiesByCell;
    }

    public static List<SymbolProbability> getExpectedSymbolProbabilitiesAcrossMatrix() {
        List<SymbolProbability> probabilities = new ArrayList<>();
        probabilities.add(new SymbolProbability("10x", 1));
        probabilities.add(new SymbolProbability("5x", 2));
        probabilities.add(new SymbolProbability("+1000", 3));
        probabilities.add(new SymbolProbability("+500", 4));
        probabilities.add(new SymbolProbability("MISS", 5));

        return probabilities;
    }

    public static List<StandardSymbol> getExpectedStandardSymbolsConfiguration() {
        List<StandardSymbol> standardSymbols = new ArrayList<>();
        standardSymbols.add(new StandardSymbol("A", BigDecimal.valueOf(5)));
        standardSymbols.add(new StandardSymbol("B", BigDecimal.valueOf(3)));
        standardSymbols.add(new StandardSymbol("C", BigDecimal.valueOf(2.5)));
        standardSymbols.add(new StandardSymbol("D", BigDecimal.valueOf(2)));
        standardSymbols.add(new StandardSymbol("E", BigDecimal.valueOf(1.2)));
        standardSymbols.add(new StandardSymbol("F", BigDecimal.valueOf(1)));

        return standardSymbols;
    }

    public static List<BonusSymbol> getExpectedBonusSymbolsConfiguration() {
        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        bonusSymbols.add(new MultiplyRewardBonusSymbol("10x", BigDecimal.valueOf(10)));
        bonusSymbols.add(new MultiplyRewardBonusSymbol("5x", BigDecimal.valueOf(5)));
        bonusSymbols.add(new ExtraBonusSymbol("+1000", BigDecimal.valueOf(1000)));
        bonusSymbols.add(new ExtraBonusSymbol("+500", BigDecimal.valueOf(500)));
        bonusSymbols.add(new MissRewardBonusSymbol());

        return bonusSymbols;
    }
}
