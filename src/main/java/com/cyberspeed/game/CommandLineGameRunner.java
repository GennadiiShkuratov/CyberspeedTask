package com.cyberspeed.game;

import com.cyberspeed.game.configuration.BasicValidationStrategy;
import com.cyberspeed.game.configuration.GameConfiguration;
import com.cyberspeed.game.configuration.GameConfigurationValidationStrategy;
import com.cyberspeed.game.configuration.SymbolsProbabilityValidationStrategy;
import com.cyberspeed.game.matrix.DefaultMatrixGenerator;
import com.cyberspeed.game.serialization.GameResultSerializer;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
                    case "--config" -> {
                        i++;
                        configFilePath = args[i];
                    }
                    case "--betting-amount" -> {
                        i++;
                        try {
                            bettingAmount = new BigDecimal(args[i]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid betting amount. Please provide a valid number value.");
                            return;
                        }
                    }
                    default -> {
                        System.out.println("Invalid argument: " + args[i]);
                        return;
                    }
                }
            }

            if(isNull(configFilePath) || isNull(bettingAmount)) {
                missedArgumentNotification();
                return;
            }

            File configFile = new File(configFilePath);
            if(!configFile.exists()){
                System.out.printf("File %s does not exist%n".formatted(configFilePath));
                return;
            }

            if (bettingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Betting amount must be non Zero positive value.");
                return;
            }

            List<GameConfigurationValidationStrategy> validationStrategies = Arrays.asList(
                    new BasicValidationStrategy(),
                    new SymbolsProbabilityValidationStrategy());
            GameConfiguration matrixGameConfiguration = GameConfiguration.buildFrom(configFile, validationStrategies);

            DefaultMatrixGenerator matrixGenerator = DefaultMatrixGenerator.builder()
                    .setColumns(matrixGameConfiguration.getColumns())
                    .setRows(matrixGameConfiguration.getRows())
                    .setSymbolProbabilitiesByCell(matrixGameConfiguration.getSymbolProbabilitiesByCell())
                    .setSymbolProbabilitiesAcrossMatrix(matrixGameConfiguration.getSymbolProbabilitiesAcrossMatrix())
                    .build();

            Game game = ScratchGame.builder()
                    .setStandardSymbols(matrixGameConfiguration.getStandardSymbols())
                    .setBonusSymbols(matrixGameConfiguration.getBonusSymbols())
                    .setWinCombinations(matrixGameConfiguration.getWinCombinations())
                    .setMatrixGenerator(matrixGenerator)
                    .build();

            Reward reward = game.play(bettingAmount);

            System.out.println(GameResultSerializer.toJson(reward));

        } catch (Exception e) {
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
