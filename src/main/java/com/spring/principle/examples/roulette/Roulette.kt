package com.spring.principle.examples.roulette

import java.math.BigDecimal


class Roulette(arg1: Int, valueOf: BigDecimal, arg3: Int) {
    val score: Int = arg1
    val probability: BigDecimal? = valueOf
    var stocks: Int? = arg3
    fun decreaseStock() {
        if (stocks!! > 0) {
            stocks = stocks!! - 1
        }
    }
}
