package com.cyberspeed.game.configuration;

public interface GameConfigurationValidationStrategy {

    void validate(GameConfiguration gameConfiguration) throws GameConfigurationValidationException;
}
