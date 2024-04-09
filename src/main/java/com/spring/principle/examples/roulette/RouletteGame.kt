package com.spring.principle.examples.roulette

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RouletteGame(val rouletteList: List<Roulette>) {

    private val soldOuts = ConcurrentHashMap<Int, Roulette>()
    private val random = Random()

    fun play(): Roulette {
        checkPlayAvailable()
        val rouletteProbability = getRouletteProbability()
        val roulette = getResult(rouletteProbability)
        decreaseStock(roulette)
        return roulette
    }

    private fun getRouletteProbability(): TreeMap<BigDecimal, Roulette> {
        val remainProbability = getCurrentProbability()
        var cumulativeProbability = BigDecimal.ZERO
        val probabilityMap = TreeMap<BigDecimal, Roulette>()

        this.rouletteList
            .filter { r -> r.stocks.get() != 0 }
            .forEach { r ->
                val add = r.probability.add(remainProbability)
                cumulativeProbability = cumulativeProbability.add(add)
                probabilityMap[cumulativeProbability] = r
            }
        return probabilityMap
    }

    private fun getResult(rouletteMap: TreeMap<BigDecimal, Roulette>): Roulette {
        val randomValue = BigDecimal.valueOf(random.nextDouble())
        return rouletteMap.higherEntry(randomValue).value
    }

    private fun decreaseStock(roulette: Roulette) {
        roulette.decreaseStock()
        if (roulette.stocks.get() == 0) {
            soldOuts[roulette.score] = roulette
        }
    }

    private fun getCurrentProbability(): BigDecimal? {
        val sumSoldOutProbability = soldOuts.values
            .stream()
            .map { obj: Roulette -> obj.probability }
            .reduce(BigDecimal.ZERO)
            { obj: BigDecimal, agent: BigDecimal? -> obj.add(agent) }
        val availableSize = rouletteList.size - soldOuts.size
        return sumSoldOutProbability.divide(BigDecimal.valueOf(availableSize.toLong()), 3, RoundingMode.HALF_UP)
    }

    fun currentTotalStock(): Int {
        return this.rouletteList
            .stream()
            .map { obj: Roulette -> obj.stocks.get() }
            .reduce(0) { a: Int, b: Int -> Integer.sum(a, b) }
    }

    private fun checkPlayAvailable() {
        if (this.rouletteList.size == this.soldOuts.size) {
            throw IllegalStateException()
        }
    }
}