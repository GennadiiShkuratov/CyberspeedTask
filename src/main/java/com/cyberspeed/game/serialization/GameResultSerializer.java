package com.cyberspeed.game.serialization;

import com.cyberspeed.game.NamedObject;
import com.cyberspeed.game.Reward;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.wincombination.WinCombination;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cyberspeed.game.Utils.roundToWholeNumber;

public class GameResultSerializer {

    private GameResultSerializer(){}

    public static String toJson(Reward reward) throws JsonProcessingException {
        Map<String, Object> gameResultProjection = new HashMap<>();

        String[][] matrix = reward.getMatrix();
        gameResultProjection.put("matrix", matrix);

        gameResultProjection.put("reward", roundToWholeNumber(reward.calculateTotalReward()));

        Map<String, List<String>> winningCombinationsProjection = new HashMap<>();
        for (Map.Entry<StandardSymbol, List<WinCombination>> winCombinationBySymbol : reward.getWinCombinations().entrySet()) {
            StandardSymbol standardSymbol = winCombinationBySymbol.getKey();
            winningCombinationsProjection.put(standardSymbol.name(),
                    winCombinationBySymbol.getValue().stream()
                            .map(WinCombination::name)
                            .toList());
        }

        gameResultProjection.put("applied_winning_combinations", winningCombinationsProjection);

        List<String> bonusSymbolsProjection = reward.getBonusSymbols().stream()
                .map(NamedObject::name)
                .toList();

        gameResultProjection.put("applied_bonus_symbol", bonusSymbolsProjection);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(gameResultProjection);
    }

}
