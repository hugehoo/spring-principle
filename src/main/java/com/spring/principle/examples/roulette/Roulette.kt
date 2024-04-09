package com.spring.principle.examples.roulette

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger


class Roulette(val score: Int, valueOf: BigDecimal, var stocks: AtomicInteger) {
    val probability: BigDecimal = valueOf
    fun decreaseStock() {
        if (stocks.get() > 0) {
            stocks.set(stocks.get() - 1)
        }
    }
}
