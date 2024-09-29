package com.cyberspeed.game;

import com.cyberspeed.game.bonus.Bonus;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.winCombination.WinCombination;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.requireNonNull;

public class Reward {

    final private String[][] matrix;
    final private BigDecimal bettingAmount;
    final private Map<StandardSymbol, List<WinCombination>> winCombinations;
    final private List<BonusSymbol> bonusSymbols;

    private Reward(String[][] matrix,
                   BigDecimal bettingAmount,
                   Map<StandardSymbol, List<WinCombination>> winCombinations,
                   List<BonusSymbol> bonusSymbols) {
        this.matrix = requireNonNull(matrix);
        this.bettingAmount = requireNonNull(bettingAmount);
        this.winCombinations = requireNonNull(winCombinations);
        this.bonusSymbols = requireNonNull(bonusSymbols);

        if(matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix can't be empty");
        }

        if (bettingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Betting amount must be non Zero positive value");
        }
    }

    public static Reward emptyReward(BigDecimal bettingAmount, String[][] matrix){
        return Reward.builder()
                .setBettingAmount(bettingAmount)
                .setMatrix(matrix)
                .setWinCombinations(Collections.emptyMap())
                .setBonusSymbols(Collections.emptyList())
                .build();
    }

    public Reward addBonusSymbols(List<BonusSymbol> bonusSymbols){
        List<BonusSymbol> totalBonusSymbols = new ArrayList<>(this.bonusSymbols);
        totalBonusSymbols.addAll(bonusSymbols);

        return Reward.builder()
                .setMatrix(this.matrix)
                .setBettingAmount(this.bettingAmount)
                .setWinCombinations(this.winCombinations)
                .setBonusSymbols(totalBonusSymbols)
                .build();
    }

    public Reward addWinCombinations(StandardSymbol symbol, List<WinCombination> winCombinations) {
        Map<StandardSymbol, List<WinCombination>> totalWinCombinations = new HashMap<>(this.winCombinations);
        totalWinCombinations.merge(symbol, winCombinations, (v1, v2) -> {
            v1.addAll(v2);
            return v1;
        });

        return Reward.builder()
                .setMatrix(this.matrix)
                .setBettingAmount(this.bettingAmount)
                .setWinCombinations(totalWinCombinations)
                .setBonusSymbols(this.bonusSymbols)
                .build();
    }

    public BigDecimal calculateTotalReward() {
        Bonus bonus = Bonus.generateFrom(bonusSymbols);

        if (bonus.missGameStatus()) return ZERO;

        BigDecimal baseReward = winCombinations.entrySet().stream()
                .map(this::accumulateWinCombinations)
                .reduce(ZERO, BigDecimal::add);

        BigDecimal totalReward = bonus.applyToReward(baseReward);
        return totalReward.setScale(0, RoundingMode.FLOOR);
    }

    private BigDecimal accumulateWinCombinations(Map.Entry<StandardSymbol, List<WinCombination>> symbolWinCombinations) {
        BigDecimal symbolMultiplier = symbolWinCombinations.getKey().getMultiplier();

        return symbolWinCombinations.getValue().stream()
                .map(WinCombination::getMultiplier)
                .reduce(bettingAmount, (v1, v2) -> v1.multiply(v2))
                .multiply(symbolMultiplier);
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public Map<StandardSymbol, List<WinCombination>> getWinCombinations() {
        return winCombinations;
    }

    public List<BonusSymbol> getBonusSymbols() {
        return bonusSymbols;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "matrix=" + Arrays.toString(matrix) +
                ", bettingAmount=" + bettingAmount +
                ", winCombinations=" + winCombinations +
                ", bonusSymbols=" + bonusSymbols +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String[][] matrix;
        private BigDecimal bettingAmount;
        private Map<StandardSymbol, List<WinCombination>> winCombinations;
        private List<BonusSymbol> bonusSymbols;

        public Builder setMatrix(String[][] matrix) {
            this.matrix = matrix;
            return this;
        }

        public Builder setBettingAmount(BigDecimal bettingAmount) {
            this.bettingAmount = bettingAmount;
            return this;
        }

        public Builder setWinCombinations(Map<StandardSymbol, List<WinCombination>> winCombinations) {
            this.winCombinations = winCombinations;
            return this;
        }

        public Builder setBonusSymbols(List<BonusSymbol> bonusSymbols) {
            this.bonusSymbols = bonusSymbols;
            return this;
        }

        public Reward build() {
            return new Reward(matrix, bettingAmount, winCombinations, bonusSymbols);
        }

    }
}
