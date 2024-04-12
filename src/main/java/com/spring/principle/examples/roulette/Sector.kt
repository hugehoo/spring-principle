package com.spring.principle.examples.roulette

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger


class Sector(val score: Int, val probability: BigDecimal, var stocks: AtomicInteger) {
    fun decreaseStock() {
        if (stocks.get() > 0) {
            stocks.set(stocks.get() - 1)
        }
    }
}
