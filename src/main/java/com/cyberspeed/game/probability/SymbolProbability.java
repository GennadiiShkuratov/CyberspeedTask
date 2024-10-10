package com.cyberspeed.game.probability;

import static java.util.Objects.requireNonNull;

public record SymbolProbability(String symbolName, int probability) {

    public SymbolProbability(String symbolName, int probability) {
        this.symbolName = requireNonNull(symbolName).toUpperCase();
        this.probability = probability;
    }

}
