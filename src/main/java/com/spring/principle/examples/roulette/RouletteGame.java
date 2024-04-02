package com.spring.principle.examples.roulette;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

import lombok.Getter;

@Getter
public class RouletteGame {

    private final List<Roulette> rouletteList;

    private final Map<Integer, Roulette> soldOuts = new HashMap<>();


    public RouletteGame(List<Roulette> roulettes) {
        this.rouletteList = roulettes;
    }

    public Roulette play() {
        Map<Integer, BigDecimal> probabilityMap = getProbabilityMap();
        TreeMap<BigDecimal, Integer> rouletteMap = buildRouletteMap(probabilityMap);
        Roulette roulette = getRouletteResult(rouletteMap);
        decreaseStock(roulette);
        return roulette;
    }

    private void decreaseStock(Roulette roulette) {
        roulette.decreaseStock();
        if (roulette.getStocks() == 0) {
            soldOuts.put(roulette.getScore(), roulette);
        }
    }

    private Roulette getRouletteResult(TreeMap<BigDecimal, Integer> rouletteMap) {
        BigDecimal randomValue = BigDecimal.valueOf(Math.random());
        Integer value = rouletteMap.higherEntry(randomValue)
            .getValue();

        return this.rouletteList.stream()
            .filter(d -> Objects.equals(d.getScore(), value))
            .findFirst()
            .orElse(null);
    }

    private TreeMap<BigDecimal, Integer> buildRouletteMap(Map<Integer, BigDecimal> probabilityMap) {
        final TreeMap<BigDecimal, Integer> rouletteMap = new TreeMap<>();
        BigDecimal cumulativeProbability = BigDecimal.ZERO;

        for (var entry : probabilityMap.entrySet()) {
            cumulativeProbability = cumulativeProbability.add(entry.getValue());
            rouletteMap.put(cumulativeProbability, entry.getKey());
        }

        return rouletteMap;
    }

    public Map<Integer, BigDecimal> getProbabilityMap() {
        BigDecimal remainProbability = getRemainProbability();
        Map<Integer, BigDecimal> probabilityMap = new HashMap<>();
        this.rouletteList.stream()
            .filter(data -> data.getStocks() != 0)
            .forEach(data -> {
                BigDecimal probability = data.getProbability();
                BigDecimal updatedProbability = probability.add(remainProbability);
                probabilityMap.put(data.getScore(), updatedProbability);
            });
        return probabilityMap;
    }

    private BigDecimal getRemainProbability() {
        BigDecimal divide = this.soldOuts.values()
            .stream()
            .map(Roulette::getProbability)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        int i = 6 - this.soldOuts.size();
        return divide.divide(BigDecimal.valueOf(i), 2, BigDecimal.ROUND_CEILING);
    }

    @Deprecated
    public Map<Integer, Roulette> getSoldOuts() {
        return this.rouletteList.stream()
            .filter(data -> data.getStocks() == 0)
            .collect(toMap(Roulette::getScore, Function.identity(), (o1, o2) -> o1));
    }
}
