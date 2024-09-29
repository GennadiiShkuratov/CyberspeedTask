package com.cyberspeed.game.configuration;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.probability.SymbolProbability;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SymbolsProbabilityValidationStrategy implements GameConfigurationValidationStrategy {
    @Override
    public void validate(GameConfiguration gameConfiguration) throws GameConfigurationValidationException {
        if(gameConfiguration.getColumns() <= 0) throw new GameConfigurationValidationException("Columns number must be bigger then Zero");
        if(gameConfiguration.getRows() <= 0) throw new GameConfigurationValidationException("Rows number must be bigger then Zero");


        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = gameConfiguration.getSymbolProbabilitiesByCell();
        for (int i = 0; i < gameConfiguration.getRows() ; i++) {
            for (int j = 0; j < gameConfiguration.getColumns() ; j++) {
                List<SymbolProbability> symbolProbabilities = symbolProbabilitiesByCell.get(new Cell(i, j));
                if(Objects.isNull(symbolProbabilities) || symbolProbabilities.isEmpty()){
                    throw new GameConfigurationValidationException(String.format("There are missed standard symbol probability configuration for cell %d:%d", i, j));
                }
            }
        }
    }
}
