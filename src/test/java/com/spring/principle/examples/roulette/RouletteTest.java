package com.spring.principle.examples.roulette;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RouletteTest {

    @Test
    void playTest() {
        List<Roulette> initialRoulette = List.of(
            new Roulette(10, BigDecimal.valueOf(0.5), 40),
            new Roulette(50, BigDecimal.valueOf(0.3), 30),
            new Roulette(100, BigDecimal.valueOf(0.2), 16),
            new Roulette(500, BigDecimal.valueOf(0.05), 8),
            new Roulette(800, BigDecimal.valueOf(0.03), 4),
            new Roulette(1000, BigDecimal.valueOf(0.02), 2));

        RouletteGame rouletteGame = new RouletteGame(initialRoulette);
        for (int i = 0; i < 100; i++) {
            rouletteGame.play();
        }

        List<Roulette> rouletteList = rouletteGame.getRouletteList();
        for (Roulette roulette : rouletteList) {
            Assertions.assertThat(roulette.getStocks()).isEqualTo(0);
        }
    }
}