package com.spring.principle.examples.roulette;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RouletteTest {
    // 요구사항
    // probability 는 합쳐서 1이 돼야한다.
    @Test
    void sample() {
        List<Roulette> roulettes = List.of(new Roulette(10, 0.5, 40),
            new Roulette(100, 0.3, 30),
            new Roulette(500, 0.2, 16),
            new Roulette(600, 0.05, 8),
            new Roulette(900, 0.03, 4),
            new Roulette(1000, 0.02, 2));

    }

}