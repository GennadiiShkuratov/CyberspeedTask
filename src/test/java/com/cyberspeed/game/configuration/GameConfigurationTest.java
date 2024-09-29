package com.cyberspeed.game.configuration;

import com.cyberspeed.game.PredefinedTestingGameConfigurationDataProvider;
import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.probability.SymbolProbability;
import com.cyberspeed.game.symbol.*;
import com.cyberspeed.game.winCombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.WinCombination;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static com.cyberspeed.game.PredefinedTestingGameConfigurationDataProvider.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GameConfigurationTest {

    @Test
    public void missedProbabilitiesForCell() throws URISyntaxException {
        //Given
        URL resource = getClass().getClassLoader().getResource("json/missed_probabilities_in_config.json");
        File configFile = Paths.get(resource.toURI()).toFile();

        //When
        Exception exception = assertThrows(GameConfigurationValidationException.class, () -> {
            GameConfiguration.buildFrom(configFile, Arrays.asList(new SymbolsProbabilityValidationStrategy()));
        });

        String expectedMessage = "There are missed standard symbol probability configuration for cell 0:3";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    public void extractedCorrectGameProperties() throws GameConfigurationValidationException, URISyntaxException {
        //Given
        URL resource = getClass().getClassLoader().getResource("json/config.json");
        File configFile = Paths.get(resource.toURI()).toFile();

        //When
        GameConfiguration gameConfiguration = GameConfiguration.buildFrom(configFile, Collections.emptyList());

        //Then
        //Matrix size check
        assertEquals(4, gameConfiguration.getRows());
        assertEquals(3, gameConfiguration.getColumns());

        //Standard symbols configuration check
        List<StandardSymbol> expectedStandardSymbolsConfiguration = getExpectedStandardSymbolsConfiguration();
        List<StandardSymbol> actualStandardSymbolsConfiguration = gameConfiguration.getStandardSymbols();
        assertThat(actualStandardSymbolsConfiguration, is(expectedStandardSymbolsConfiguration));

        //Standard symbols configuration check
        List<BonusSymbol> expectedBonusSymbolsConfiguration = getExpectedBonusSymbolsConfiguration();
        List<BonusSymbol> actualBonusSymbolsConfiguration = gameConfiguration.getBonusSymbols();
        assertThat(expectedBonusSymbolsConfiguration, is(actualBonusSymbolsConfiguration));

        //Symbol probabilities configuration check
        List<SymbolProbability> expectedSymbolProbabilitiesAcrossMatrix = getExpectedSymbolProbabilitiesAcrossMatrix();
        List<SymbolProbability> actualSymbolProbabilitiesAcrossMatrix = gameConfiguration.getSymbolProbabilitiesAcrossMatrix();
        assertThat(expectedSymbolProbabilitiesAcrossMatrix, is(actualSymbolProbabilitiesAcrossMatrix));

        Map<Cell, List<SymbolProbability>> expectedSymbolProbabilitiesByCell = getExpectedSymbolProbabilitiesByCell();
        Map<Cell, List<SymbolProbability>> actualSymbolProbabilitiesByCell = gameConfiguration.getSymbolProbabilitiesByCell();
        assertThat(expectedSymbolProbabilitiesByCell, is(actualSymbolProbabilitiesByCell));

        //Winning combinations configuration check
        List<WinCombination> expectedWinCombinations = getExpectedWinCombinations();
        List<WinCombination> actualWinCombinations = gameConfiguration.getWinCombinations();
        assertThat(expectedWinCombinations, is(actualWinCombinations));

    }






}