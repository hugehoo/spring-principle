package com.spring.principle.examples.roulette;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RouletteGame {

    private final List<Roulette> rouletteList;

    private final Map<Integer, Roulette> soldOuts = new HashMap<>();


    public RouletteGame(List<Roulette> roulettes) {
        this.rouletteList = roulettes;
    }

    public Roulette play() {
        TreeMap<BigDecimal, Roulette> rouletteProbability = getRouletteProbability();
        Roulette roulette = getResult(rouletteProbability);
        decreaseStock(roulette);
        return roulette;
    }

    private TreeMap<BigDecimal, Roulette> getRouletteProbability() {
        BigDecimal remainProbability = getCurrentProbability();
        BigDecimal cumulativeProbability = BigDecimal.ZERO;
        TreeMap<BigDecimal, Roulette> probabilityMap = new TreeMap<>();
        for (Roulette roulette : this.rouletteList) {
            if (roulette.getStocks() != 0) {
                BigDecimal updatedProbability = roulette.getProbability().add(remainProbability);
                cumulativeProbability = cumulativeProbability.add(updatedProbability);
                probabilityMap.put(cumulativeProbability, roulette);
            }
        }
        return probabilityMap;
    }

    private Roulette getResult(TreeMap<BigDecimal, Roulette> rouletteMap) {
        BigDecimal randomValue = BigDecimal.valueOf(Math.random());
        return rouletteMap.higherEntry(randomValue)
            .getValue();
    }

    private void decreaseStock(Roulette roulette) {
        roulette.decreaseStock();
        if (roulette.getStocks() == 0) {
            soldOuts.put(roulette.getScore(), roulette);
        }
    }

    private BigDecimal getCurrentProbability() {
        BigDecimal sumSoldOutProbability = this.soldOuts.values()
            .stream()
            .map(Roulette::getProbability)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        int availableSize = rouletteList.size() - this.soldOuts.size();
        return sumSoldOutProbability.divide(BigDecimal.valueOf(availableSize), 3, RoundingMode.HALF_UP);
    }

}
