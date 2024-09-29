package com.cyberspeed.game;

import com.cyberspeed.game.matrix.MatrixGenerator;
import com.cyberspeed.game.probability.SymbolProbability;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.winCombination.WinCombination;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class ScratchGame implements Game {

    final private Map<String, StandardSymbol> standardSymbolsByName;
    final private Map<String, BonusSymbol> bonusSymbolsByName;
    final private List<WinCombination> winCombinations;
    final private MatrixGenerator matrixGenerator;

    private ScratchGame(List<StandardSymbol> standardSymbols,
                        List<BonusSymbol> bonusSymbols,
                        List<WinCombination> winCombinations,
                        MatrixGenerator matrixGenerator) {
        this.standardSymbolsByName = requireNonNull(standardSymbols).stream()
                .collect(collectingAndThen(
                        toMap(v -> v.getName(), identity()),
                        Collections::unmodifiableMap
                ));

        if(standardSymbolsByName.isEmpty()) {
            throw new IllegalArgumentException("standardSymbols list can't be empty or null");
        }

        this.bonusSymbolsByName = requireNonNull(bonusSymbols).stream()
                .collect(collectingAndThen(
                        toMap(v -> v.getName(), identity()),
                        Collections::unmodifiableMap
                ));

        this.matrixGenerator = requireNonNull(matrixGenerator);
        this.winCombinations = requireNonNull(winCombinations);
    }


    public Reward play(BigDecimal bettingAmount) {
        requireNonNull(bettingAmount);
        if (bettingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("BettingAmount have to be positive not Zero value");
        }

        String[][] matrix = matrixGenerator.generateMatrix();

        Reward reward = Reward.emptyReward(bettingAmount, matrix);

        for (StandardSymbol symbol : standardSymbolsByName.values()) {
            Map<String, WinCombination> multiplierByWinCombinationGroup = new HashMap<>();

            for (WinCombination winCombination : winCombinations) {
                if (winCombination.isApplicable(symbol, matrix)) {
                    String group = winCombination.getGroup();
                    multiplierByWinCombinationGroup.merge(group,
                            winCombination,
                            (v1, v2) -> v1.getMultiplier().compareTo(v2.getMultiplier()) < 0 ? v2 : v1);
                }
            }

            reward = multiplierByWinCombinationGroup.isEmpty()
                    ? reward
                    : reward.addWinCombinations(symbol, new ArrayList<>(multiplierByWinCombinationGroup.values()));
        }

        List<BonusSymbol> allBonusSymbols = getAllBonusSymbols(matrix);
        reward = allBonusSymbols.isEmpty() ? reward : reward.addBonusSymbols(allBonusSymbols);

        return reward;
    }

    private List<BonusSymbol> getAllBonusSymbols(String[][] matrix) {
        return Arrays.stream(matrix)
                .flatMap((Arrays::stream))
                .map(v -> bonusSymbolsByName.get(v))
                .filter(Objects::nonNull)
                .map(BonusSymbol.class::cast)
                .collect(toList());

    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private List<StandardSymbol> standardSymbols;
        private List<BonusSymbol> bonusSymbols;
        private List<WinCombination> winCombinations;
        private MatrixGenerator matrixGenerator;

        public Builder setMatrixGenerator(MatrixGenerator matrixGenerator) {
            this.matrixGenerator = matrixGenerator;
            return this;
        }

        public Builder setStandardSymbols(List<StandardSymbol> standardSymbols) {
            this.standardSymbols = standardSymbols;
            return this;
        }

        public Builder setBonusSymbols(List<BonusSymbol> bonusSymbols) {
            this.bonusSymbols = bonusSymbols;
            return this;
        }

        public Builder setWinCombinations(List<WinCombination> winCombinations) {
            this.winCombinations = winCombinations;
            return this;
        }

        public ScratchGame build() {
            return new ScratchGame(standardSymbols, bonusSymbols, winCombinations, matrixGenerator);
        }
    }

}
