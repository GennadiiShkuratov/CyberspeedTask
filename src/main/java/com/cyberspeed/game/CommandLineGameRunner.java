package com.cyberspeed.game;

import com.cyberspeed.game.configuration.*;
import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.matrix.DefaultMatrixGenerator;
import com.cyberspeed.game.probability.SymbolProbability;
import com.cyberspeed.game.serialization.GameResultSerializer;
import com.cyberspeed.game.symbol.BonusSymbol;
import com.cyberspeed.game.symbol.StandardSymbol;
import com.cyberspeed.game.winCombination.WinCombination;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.isNull;

public class CommandLineGameRunner {

    public static void main(String[] args) {
        try {
            if (args.length != 4) {
                missedArgumentNotification();
                return;
            }

            BigDecimal bettingAmount = null;
            String configFilePath = null;
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--config":
                        configFilePath = args[++i];
                        break;
                    case "--betting-amount":
                        try {
                            bettingAmount = new BigDecimal(args[++i]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid betting amount. Please provide a valid number value.");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid argument: " + args[i]);
                        return;
                }
            }

            if(isNull(configFilePath) || isNull(bettingAmount)) {
                missedArgumentNotification();
                return;
            }

            if (bettingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Betting amount must be non Zero positive value.");
                return;
            }

            List<GameConfigurationValidationStrategy> validationStrategies = Arrays.asList(
                    new BasicValidationStrategy(),
                    new SymbolsProbabilityValidationStrategy());
            GameConfiguration matrixGameConfiguration = GameConfiguration.buildFrom(new File(configFilePath), validationStrategies);

            int columns = matrixGameConfiguration.getColumns();
            int rows = matrixGameConfiguration.getRows();
            List<StandardSymbol> standardSymbols = matrixGameConfiguration.getStandardSymbols();
            List<BonusSymbol> bonusSymbols = matrixGameConfiguration.getBonusSymbols();
            Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = matrixGameConfiguration.getSymbolProbabilitiesByCell();
            List<SymbolProbability> symbolProbabilitiesAcrossMatrix = matrixGameConfiguration.getSymbolProbabilitiesAcrossMatrix();
            List<WinCombination> winCombinations = matrixGameConfiguration.getWinCombinations();

            DefaultMatrixGenerator matrixGenerator = DefaultMatrixGenerator.builder()
                    .setColumns(columns)
                    .setRows(rows)
                    .setSymbolProbabilitiesByCell(symbolProbabilitiesByCell)
                    .setSymbolProbabilitiesAcrossMatrix(symbolProbabilitiesAcrossMatrix)
                    .build();

            Game game = ScratchGame.builder()
                    .setStandardSymbols(standardSymbols)
                    .setBonusSymbols(bonusSymbols)
                    .setWinCombinations(winCombinations)
                    .setMatrixGenerator(matrixGenerator)
                    .build();

            Reward reward = game.play(bettingAmount);

            System.out.println(GameResultSerializer.toJson(reward));

        } catch (Throwable e) {
            System.out.println("Failed to run game. Details: " + e.getMessage());

        }
    }


    private static void missedArgumentNotification() {
        System.out.println("""
                Missed one of arguments:
                config          |   config file which is described top of the document
                betting amount  |   betting amount  
                Please provide both arguments in next format: --config config.json --betting-amount 100
                """);
    }


}
