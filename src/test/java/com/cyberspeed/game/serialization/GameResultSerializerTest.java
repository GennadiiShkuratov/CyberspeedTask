package com.cyberspeed.game.serialization;

import com.cyberspeed.game.Reward;
import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.ExtraBonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.wincombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.WinCombination;
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
    void serializeResultShouldContainAllAttributes() throws JsonProcessingException {
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

        String expectedResult = """
                    {
                    "matrix": [
                        ["A", "A", "B"],
                        ["A", "+1000", "B"],
                        ["A", "A", "B"]
                    ],
                    "reward": 1800,
                    "applied_winning_combinations": {
                        "A": ["same_symbol_5_times", "same_symbols_vertically"],
                        "B": ["same_symbol_3_times", "same_symbols_vertically"]
                    },
                   "applied_bonus_symbol": [
                        "+1000"
                   ]
                }
                """;

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