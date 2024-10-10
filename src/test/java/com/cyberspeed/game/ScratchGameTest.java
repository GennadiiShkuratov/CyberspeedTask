package com.cyberspeed.game;

import com.cyberspeed.game.configuration.GameConfiguration;
import com.cyberspeed.game.configuration.GameConfigurationValidationException;
import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.matrix.MatrixGenerator;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.wincombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.WinCombination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

class ScratchGameTest {

    @Test
    void oneWinCombinationFromSameGroupShouldBeApplied() {
        //Given
        List<StandardSymbol> standardSymbols = new ArrayList<>();
        standardSymbols.add(new StandardSymbol("A", BigDecimal.valueOf(2)));
        standardSymbols.add(new StandardSymbol("E", BigDecimal.valueOf(20)));
        standardSymbols.add(new StandardSymbol("F", BigDecimal.valueOf(200)));

        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        List<WinCombination> winCombinations = new ArrayList<>();
        winCombinations.add(
                new WinCombination("same_symbols_vertically",
                        "linear_symbols",
                        BigDecimal.valueOf(5),
                        "linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
                                new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
                                new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                        })));

        winCombinations.add(
                new WinCombination("same_symbols_horizontally",
                        "linear_symbols",
                        BigDecimal.valueOf(4),
                        "linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)},
                                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)},
                                new Cell[]{new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)}
                        })));

        MatrixGenerator matrixGenerator = () -> new String[][]{
                new String[]{ "A", "A", "A" },
                new String[]{ "A", "E", "A" },
                new String[]{ "A", "F", "A" }
        };

        ScratchGame game = ScratchGame.builder()
                .setStandardSymbols(standardSymbols)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .setMatrixGenerator(matrixGenerator)
                .build();

        //When
        Reward reward = game.play(new BigDecimal(100));

        //Then
        BigDecimal actualRewardValue = reward.calculateTotalReward();
        BigDecimal expectedRewardValue = new BigDecimal(1000);
        Assertions.assertEquals(expectedRewardValue, actualRewardValue);

    }

    @Test
    void rewardByFSymbolUsing3SameSymbolsCombinationAndExtra500() throws URISyntaxException, GameConfigurationValidationException {
        //Given
        URL resource = getClass().getClassLoader().getResource("json/config.json");
        File configFile = Paths.get(resource.toURI()).toFile();

        GameConfiguration matrixGameConfiguration = GameConfiguration.buildFrom(configFile, emptyList());

        List<StandardSymbol> standardSymbols = matrixGameConfiguration.getStandardSymbols();
        List<BonusSymbol> bonusSymbols = matrixGameConfiguration.getBonusSymbols();
        List<WinCombination> winCombinations = matrixGameConfiguration.getWinCombinations();

        MatrixGenerator matrixGenerator = () -> new String[][]{
                new String[]{"+500", "F", "D" },
                new String[]{"F", "C", "F" },
                new String[]{"C", "D", "A" }
        };

        ScratchGame game = ScratchGame.builder()
                .setStandardSymbols(standardSymbols)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .setMatrixGenerator(matrixGenerator)
                .build();

        //When
        Reward reward = game.play(new BigDecimal(50));

        //Then
        BigDecimal actualRewardValue = reward.calculateTotalReward();
        BigDecimal expectedRewardValue = new BigDecimal(550);
        Assertions.assertEquals(expectedRewardValue, actualRewardValue);

    }

    @Test
    void rewardByESymbolUsing3SameSymbolsCombinationAndFSymbolUsing3SameSymbolsCombination() throws URISyntaxException, GameConfigurationValidationException {
        //Given
        URL resource = getClass().getClassLoader().getResource("json/config.json");
        File configFile = Paths.get(resource.toURI()).toFile();

        GameConfiguration matrixGameConfiguration = GameConfiguration.buildFrom(configFile, emptyList());

        List<StandardSymbol> standardSymbols = matrixGameConfiguration.getStandardSymbols();
        List<BonusSymbol> bonusSymbols = matrixGameConfiguration.getBonusSymbols();
        List<WinCombination> winCombinations = matrixGameConfiguration.getWinCombinations();

        MatrixGenerator matrixGenerator = () -> new String[][]{
                new String[]{ "E", "E", "F" },
                new String[]{ "C", "E", "F" },
                new String[]{ "C", "F", "D" }
        };

        ScratchGame game = ScratchGame.builder()
                .setStandardSymbols(standardSymbols)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .setMatrixGenerator(matrixGenerator)
                .build();

        //When
        Reward reward = game.play(new BigDecimal(100));

        //Then
        BigDecimal actualRewardValue = reward.calculateTotalReward();
        BigDecimal expectedRewardValue = new BigDecimal(220);
        Assertions.assertEquals(expectedRewardValue, actualRewardValue);

    }


}