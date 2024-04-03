package com.spring.principle.examples.roulette;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RouletteTest {

    final int INITIAL_STOCK = 100;

    private List<Roulette> 룰렛_초기화() {
        return List.of(
            new Roulette(10, BigDecimal.valueOf(0.5), 40),
            new Roulette(50, BigDecimal.valueOf(0.3), 30),
            new Roulette(100, BigDecimal.valueOf(0.1), 16),
            new Roulette(500, BigDecimal.valueOf(0.05), 8),
            new Roulette(800, BigDecimal.valueOf(0.03), 4),
            new Roulette(1000, BigDecimal.valueOf(0.02), 2));
    }

    @Test
    @DisplayName("룰렛 게임 플레이 테스트")
    void playTest() {
        List<Roulette> initialRoulette = 룰렛_초기화();

        RouletteGame rouletteGame = new RouletteGame(initialRoulette);
        for (int i = 0; i < INITIAL_STOCK; i++) {
            Roulette play = rouletteGame.play();
            if (play.getStocks() == 0) {
                System.out.println(i + "번째 play: " + play.getScore() + "점 아이템 소진");
            }
        }

        List<Roulette> rouletteList = rouletteGame.getRouletteList();
        for (Roulette roulette : rouletteList) {
            Assertions.assertThat(roulette.getStocks()).isEqualTo(0);
        }
    }


    @Test
    @DisplayName("플레이 마다 재고는 1씩 차감된다")
    void stockCountTest() {

        List<Roulette> initialRoulette = 룰렛_초기화();

        RouletteGame rouletteGame = new RouletteGame(initialRoulette);
        Integer totalStock = rouletteGame.getCurrentTotalStock();

        Assertions.assertThat(totalStock).isEqualTo(INITIAL_STOCK);
        for (int i = 1; i <= INITIAL_STOCK; i++) {
            rouletteGame.play();
            Integer currentStock = rouletteGame.getCurrentTotalStock();
            Assertions.assertThat(currentStock).isEqualTo(INITIAL_STOCK - i);
        }
    }

    @Test
    @DisplayName("재고 없을때 예외를 던진다")
    void overPlayTest() {
        List<Roulette> initialRoulette = 룰렛_초기화();

        RouletteGame rouletteGame = new RouletteGame(initialRoulette);
        for (int i = 1; i <= 100; i++) {
            rouletteGame.play();
        }
        Assertions.assertThatThrownBy(rouletteGame::play);
    }

    @Test
    @DisplayName("동시성 테스트")
    void multiThreadTest() throws InterruptedException, ExecutionException {
        List<Roulette> initialRoulette = 룰렛_초기화();
        final int INITIAL_STOCK = initialRoulette.stream()
            .mapToInt(Roulette::getStocks)
            .sum();

        RouletteGame rouletteGame = new RouletteGame(initialRoulette);

        int numThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Roulette>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Future<Roulette> future = executorService.submit(rouletteGame::play);
            future.get();
        }
        executorService.shutdown();

        Assertions.assertThat(INITIAL_STOCK - numThreads)
            .isEqualTo(rouletteGame.getCurrentTotalStock());
    }
}
