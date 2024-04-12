@file:Suppress("NonAsciiCharacters")

package com.spring.principle.examples.roulette

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

internal class RouletteUnitTest {
    private val initialStock = 100
    private fun 룰렛_초기화(): List<Sector> {
        return listOf(
            Sector(10, BigDecimal.valueOf(0.5), AtomicInteger(40)),
            Sector(50, BigDecimal.valueOf(0.3), AtomicInteger(30)),
            Sector(100, BigDecimal.valueOf(0.1), AtomicInteger(16)),
            Sector(500, BigDecimal.valueOf(0.05), AtomicInteger(8)),
            Sector(800, BigDecimal.valueOf(0.03), AtomicInteger(4)),
            Sector(1000, BigDecimal.valueOf(0.02), AtomicInteger(2))
        )
    }

    @Test
    @DisplayName("룰렛 게임 플레이 테스트")
    fun playTest() {
        val initialRoulette = 룰렛_초기화()
        val rouletteGame = RouletteGame(initialRoulette)
        for (i in 0 until initialStock) {
            val play = rouletteGame.play()
            if (play.stocks.get() == 0) {
                println(i.toString() + "번째 play: " + play.score + "점 아이템 소진")
            }
        }
        val rouletteList = rouletteGame.sectorList
        for (roulette in rouletteList) {
            Assertions.assertThat(roulette.stocks.get()).isEqualTo(0)
        }
    }

    @Test
    @DisplayName("플레이 마다 재고는 1씩 차감된다")
    fun stockCountTest() {
        val initialRoulette = 룰렛_초기화()
        val rouletteGame = RouletteGame(initialRoulette)
        val totalStock = rouletteGame.currentTotalStock()
        Assertions.assertThat(totalStock).isEqualTo(initialStock)
        for (i in 1..initialStock) {
            rouletteGame.play()
            val currentStock = rouletteGame.currentTotalStock()
            Assertions.assertThat(currentStock).isEqualTo(initialStock - i)
        }
    }

    @Test
    @DisplayName("재고 없을때 예외를 던진다")
    fun overPlayTest() {
        val initialRoulette = 룰렛_초기화()
        val rouletteGame = RouletteGame(initialRoulette)
        for (i in 1..100) {
            rouletteGame.play()
        }
        Assertions.assertThatThrownBy { rouletteGame.play() }
    }

    @Test
    @DisplayName("스레드 30개 동시성 테스트 - Future")
    @Throws(
        InterruptedException::class,
        ExecutionException::class
    )
    fun multiThreadTest() {
        val initialRoulette = 룰렛_초기화()
        val initialStock = initialRoulette.stream()
            .mapToInt { r -> r.stocks.get() }
            .sum()
        val rouletteGame = RouletteGame(initialRoulette)
        val numThreads = 30
        val executorService = Executors.newFixedThreadPool(numThreads)
        for (i in 0 until numThreads) {
            val future = executorService.submit<Sector> { rouletteGame.play() }
            future.get()
        }
        executorService.shutdown()
        Assertions.assertThat(initialStock - numThreads)
            .isEqualTo(rouletteGame.currentTotalStock())
    }

    @Test
    @DisplayName("스레드 30개 동시성 테스트 - Thread")
    @Throws(
        InterruptedException::class
    )
    fun multiThreadTestV2() {
        val initialRoulette = 룰렛_초기화()
        val rouletteGame = RouletteGame(initialRoulette)
        val numThreads = 30
        val threads: MutableList<Thread> = ArrayList()
        for (i in 0 until numThreads) {
            val thread = Thread { rouletteGame.play() }
            threads.add(thread)
            thread.start()
        }
        for (thread in threads) {
            thread.join()
        }
        Assertions.assertThat(100 - numThreads).isEqualTo(rouletteGame.currentTotalStock())
    }
}