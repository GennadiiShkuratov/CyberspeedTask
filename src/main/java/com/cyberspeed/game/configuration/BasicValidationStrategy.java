package com.cyberspeed.game.configuration;

public class BasicValidationStrategy implements GameConfigurationValidationStrategy {
    @Override
    public void validate(GameConfiguration gameConfiguration) throws GameConfigurationValidationException {
        if(gameConfiguration.getColumns() <= 0) throw new GameConfigurationValidationException("Columns number must be bigger then Zero");
        if(gameConfiguration.getRows() <= 0) throw new GameConfigurationValidationException("Rows number must be bigger then Zero");

        if(gameConfiguration.getStandardSymbols().isEmpty()) {
            throw new GameConfigurationValidationException("Configuration does not provide symbols details");
        }

        if(gameConfiguration.getSymbolProbabilitiesByCell().isEmpty() &&
                gameConfiguration.getSymbolProbabilitiesAcrossMatrix().isEmpty()) {
            throw new GameConfigurationValidationException("Configuration does not provide symbols probability details");
        }

        if(gameConfiguration.getWinCombinations().isEmpty()) {
            throw new GameConfigurationValidationException("Configuration does not provide symbols probability details");
        }

    }
}
