package com.cyberspeed.game.probability;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SymbolProbability {

    private final String symbolName;
    private final int probability;

    public SymbolProbability(String symbolName, int probability) {
        this.symbolName = requireNonNull(symbolName).toUpperCase();
        this.probability = probability;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public int getProbability() {
        return probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolProbability that = (SymbolProbability) o;
        return probability == that.probability && Objects.equals(symbolName, that.symbolName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolName, probability);
    }

    @Override
    public String toString() {
        return "SymbolProbability{" +
                "symbolName='" + symbolName + '\'' +
                ", probability=" + probability +
                '}';
    }
}
