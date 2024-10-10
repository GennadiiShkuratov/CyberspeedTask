package com.cyberspeed.game.configuration;

import com.cyberspeed.game.matrix.Cell;
import com.cyberspeed.game.probability.SymbolProbability;
import com.cyberspeed.game.symbol.*;
import com.cyberspeed.game.wincombination.LinearSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.SameSymbolsCombinationStrategy;
import com.cyberspeed.game.wincombination.WinCombination;
import com.cyberspeed.game.wincombination.WinCombinationStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

public class GameConfiguration {

    private final int columns;
    private final int rows;
    private final List<StandardSymbol> standardSymbols;
    private final List<BonusSymbol> bonusSymbols;
    private final List<WinCombination> winCombinations;
    private final Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell;
    private final List<SymbolProbability> symbolProbabilitiesAcrossMatrix;

    private GameConfiguration(int columns,
                              int rows,
                              List<StandardSymbol> standardSymbols,
                              List<BonusSymbol> bonusSymbols,
                              List<WinCombination> winCombinations,
                              Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell,
                              List<SymbolProbability> symbolProbabilitiesAcrossMatrix) {
        if(columns <= 0) throw new IllegalArgumentException("Columns number must be bigger then Zero");
        if(rows <= 0) throw new IllegalArgumentException("Rows number must be bigger then Zero");

        this.columns = columns;
        this.rows = rows;
        this.standardSymbols = unmodifiableList(requireNonNull(standardSymbols));
        this.bonusSymbols =unmodifiableList(requireNonNull(bonusSymbols));
        this.winCombinations = unmodifiableList(requireNonNull(winCombinations));
        this.symbolProbabilitiesByCell = unmodifiableMap(requireNonNull(symbolProbabilitiesByCell));
        this.symbolProbabilitiesAcrossMatrix = unmodifiableList(requireNonNull(symbolProbabilitiesAcrossMatrix));

    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public List<StandardSymbol> getStandardSymbols() {
        return standardSymbols;
    }

    public List<BonusSymbol> getBonusSymbols() {
        return bonusSymbols;
    }

    public List<WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public Map<Cell, List<SymbolProbability>> getSymbolProbabilitiesByCell() {
        return symbolProbabilitiesByCell;
    }

    public List<SymbolProbability> getSymbolProbabilitiesAcrossMatrix() {
        return symbolProbabilitiesAcrossMatrix;
    }

    public static GameConfiguration buildFrom(File configFile, List<GameConfigurationValidationStrategy> validationStrategies) throws GameConfigurationValidationException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(configFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong json format of configuration file: " + e.getMessage(), e);
        }

        int columns = rootNode.path("columns").asInt();
        int rows = rootNode.path("rows").asInt();

        List<StandardSymbol> standardSymbols = extractStandardSymbol(rootNode);
        List<BonusSymbol> bonusSymbols = extractBonusSymbols(rootNode);

        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = extractSymbolProbabilitiesByCell(rootNode);
        List<SymbolProbability> symbolProbabilitiesAcrossMatrix = extractSymbolProbabilitiesAcrossMatrix(rootNode);

        List<WinCombination> winCombinations = parseWinCombinations(rootNode);

        GameConfiguration gameConfiguration = new GameConfiguration(
                columns,
                rows,
                standardSymbols,
                bonusSymbols,
                winCombinations,
                symbolProbabilitiesByCell,
                symbolProbabilitiesAcrossMatrix
        );

        for (GameConfigurationValidationStrategy validationStrategy : validationStrategies) {
            validationStrategy.validate(gameConfiguration);
        }

        return gameConfiguration;
    }


    private static Map<Cell, List<SymbolProbability>> extractSymbolProbabilitiesByCell(JsonNode rootNode) {
        Map<Cell, List<SymbolProbability>> symbolProbabilitiesByCell = new HashMap<>();
        JsonNode standardSymbolsNode = rootNode.path("probabilities").path("standard_symbols");

        for (JsonNode cellNode : standardSymbolsNode) {
            int col = cellNode.path("column").asInt();
            int row = cellNode.path("row").asInt();
            Cell cell = new Cell(row, col);

            JsonNode symbolsNode = cellNode.path("symbols");
            List<SymbolProbability> probabilities = new ArrayList<>();
            symbolsNode.fields().forEachRemaining(entry -> {
                String symbol = entry.getKey();
                int probability = entry.getValue().asInt();
                probabilities.add(new SymbolProbability(symbol, probability));
            });

            symbolProbabilitiesByCell.put(cell, probabilities);
        }

        return symbolProbabilitiesByCell;
    }

    private static List<SymbolProbability> extractSymbolProbabilitiesAcrossMatrix(JsonNode rootNode) {
        List<SymbolProbability> symbolProbabilitiesAcrossMatrix = new ArrayList<>();
        JsonNode bonusSymbolsNode = rootNode.path("probabilities").path("bonus_symbols").path("symbols");

        bonusSymbolsNode.fields().forEachRemaining(entry -> symbolProbabilitiesAcrossMatrix.add(
                new SymbolProbability(
                        entry.getKey(),
                        entry.getValue().asInt())));

        return symbolProbabilitiesAcrossMatrix;
    }

    private static List<WinCombination> parseWinCombinations(JsonNode rootNode) {
        List<WinCombination> winCombinations = new ArrayList<>();
        JsonNode winCombinationsNode = rootNode.path("win_combinations");

        winCombinationsNode.fields().forEachRemaining(entry -> winCombinations.add(extractWinCombination(entry)));

        return winCombinations;
    }

    private static WinCombination extractWinCombination(Map.Entry<String, JsonNode> entry) {
        String name = entry.getKey();
        JsonNode winCombinationNode = entry.getValue();

        BigDecimal multiplier = BigDecimal.valueOf(winCombinationNode.path("reward_multiplier").asDouble());
        String when = winCombinationNode.path("when").asText();
        String group = winCombinationNode.path("group").asText();

        WinCombinationStrategy strategy =
            switch (entry.getValue().path("group").asText()) {
                case "same_symbols" -> {
                    int count = winCombinationNode.path("count").asInt();
                    yield new SameSymbolsCombinationStrategy(count);
                }
                case "horizontally_linear_symbols",
                     "vertically_linear_symbols",
                     "ltr_diagonally_linear_symbols",
                     "rtl_diagonally_linear_symbols" -> {
                    Cell[][] coveredArea = getCoveredAreas(winCombinationNode.path("covered_areas"));
                    yield new LinearSymbolsCombinationStrategy(coveredArea);
                }
                default ->
                        throw new RuntimeException("Configuration file contains unknown impact type for bonus symbol: " + entry.getKey());
            };

        return new WinCombination(name, when, multiplier, group, strategy);
    }

    private static Cell[][] getCoveredAreas(JsonNode coveredAreaNode) {
        List<Cell[]> coveredAreasList = new ArrayList<>();

        for (JsonNode rowNode : coveredAreaNode) {
            List<Cell> cellList = new ArrayList<>();

            for (JsonNode cellStringNode : rowNode) {
                String[] parts = cellStringNode.asText().split(":");
                int rowIndex = Integer.parseInt(parts[0]);
                int columnIndex = Integer.parseInt(parts[1]);
                cellList.add(new Cell(rowIndex, columnIndex));
            }
            coveredAreasList.add(cellList.toArray(new Cell[0]));
        }

        return coveredAreasList.toArray(new Cell[0][]);
    }

    private static List<StandardSymbol> extractStandardSymbol(JsonNode rootNode) {
        JsonNode symbolsNode = rootNode.path("symbols");
        List<StandardSymbol> symbols = new ArrayList<>();
        symbolsNode.fields().forEachRemaining(entry -> {
            if ("standard".equals(entry.getValue().path("type").asText())) {
                StandardSymbol symbol = new StandardSymbol(
                        entry.getKey(),
                        BigDecimal.valueOf(entry.getValue().path("reward_multiplier").asDouble()));
                symbols.add(symbol);
            }
        });

        return symbols;
    }

    private static List<BonusSymbol> extractBonusSymbols(JsonNode rootNode) {
        JsonNode symbolsNode = rootNode.path("symbols");
        List<BonusSymbol> symbols = new ArrayList<>();
        symbolsNode.fields().forEachRemaining(entry -> {
            if ("bonus".equals(entry.getValue().path("type").asText())) {
                BonusSymbol symbol = extractBonusSymbol(entry);
                symbols.add(symbol);
            }
        });

        return symbols;
    }

    private static BonusSymbol extractBonusSymbol(Map.Entry<String, JsonNode> entry) {
        return switch (entry.getValue().path("impact").asText()) {
            case "multiply_reward" -> new MultiplyRewardBonusSymbol(
                    entry.getKey(),
                    BigDecimal.valueOf(entry.getValue().path("reward_multiplier").asDouble()));
            case "extra_bonus" -> new ExtraBonusSymbol(
                    entry.getKey(),
                    BigDecimal.valueOf(entry.getValue().path("extra").asDouble()));
            case "miss" -> new MissRewardBonusSymbol();
            default ->
                    throw new RuntimeException("Configuration file contains unknown impact type for bonus symbol: " + entry.getKey());
        };
    }


}
