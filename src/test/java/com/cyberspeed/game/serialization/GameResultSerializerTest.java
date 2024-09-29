package com.cyberspeed.game.serialization;

import com.cyberspeed.game.Reward;
import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.ExtraBonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.winCombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.winCombination.WinCombination;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;

class GameResultSerializerTest {

    @Test
    public void serializeResultShouldContainAllAttributes() throws JsonProcessingException {
        //Given
        String[][] matrix = new String[][] {
                new String[]{"A", "A", "B"},
                new String[]{"A", "+1000", "B"},
                new String[]{"A", "A", "B"},
        };

        List<BonusSymbol> bonusSymbols = new ArrayList<>();
        bonusSymbols.add(new ExtraBonusSymbol("+1000", new BigDecimal(1000)));

        BigDecimal bettingAmount = BigDecimal.valueOf(100);

        Map<StandardSymbol, List<WinCombination>> winCombinations = new HashMap<>();
        winCombinations.put(new StandardSymbol("A", ONE), Arrays.asList(
                new WinCombination("same_symbol_5_times", "same_symbols", BigDecimal.valueOf(2), "same_symbols", new SameSymbolsCombinationStrategy((5))),
                new WinCombination("same_symbols_vertically", "linear_symbols", BigDecimal.valueOf(2), "vertically_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{new Cell(0, 1), new Cell(1, 0), new Cell(2, 0)}
                        })
                )
        ));
        winCombinations.put(new StandardSymbol("B", BigDecimal.valueOf(2)), Arrays.asList(
                new WinCombination("same_symbol_3_times", "", ONE, "", new SameSymbolsCombinationStrategy((3))),
                new WinCombination("same_symbols_vertically", "linear_symbols", BigDecimal.valueOf(2), "vertically_linear_symbols",
                        new LinearSymbolsCombinationStrategy(new Cell[][]{
                                new Cell[]{
                                        new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
                        })
                )
        ));

        Reward reward = Reward.builder()
                .setMatrix(matrix)
                .setBettingAmount(bettingAmount)
                .setBonusSymbols(bonusSymbols)
                .setWinCombinations(winCombinations)
                .build();

        String expectedResult = "{\n" +
                "    \"matrix\": [\n" +
                "        [\"A\", \"A\", \"B\"],\n" +
                "        [\"A\", \"+1000\", \"B\"],\n" +
                "        [\"A\", \"A\", \"B\"]\n" +
                "    ],\n" +
                "    \"reward\": 1800,\n" +
                "    \"applied_winning_combinations\": {\n" +
                "        \"A\": [\"same_symbol_5_times\", \"same_symbols_vertically\"],\n" +
                "        \"B\": [\"same_symbol_3_times\", \"same_symbols_vertically\"]\n" +
                "    },\n" +
                "   \"applied_bonus_symbol\": [\n" +
                "        \"+1000\"\n" +
                "   ]\n" +
                "}";

        //When
        String actualResult = GameResultSerializer.toJson(reward);

        System.out.println(actualResult);
        //Then
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJsonNode = objectMapper.readTree(expectedResult);
        JsonNode actualJsonNode = objectMapper.readTree(actualResult);
        Assertions.assertEquals(expectedJsonNode, actualJsonNode);
    }

}