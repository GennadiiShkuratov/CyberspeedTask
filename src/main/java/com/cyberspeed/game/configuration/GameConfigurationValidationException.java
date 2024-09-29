package com.cyberspeed.game.configuration;

public class GameConfigurationValidationException extends Exception {
    public GameConfigurationValidationException(String message) {
        super(message);
    }

    public GameConfigurationValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
