package com.spring.principle.examples.roulette

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RouletteGame(val sectorList: List<Sector>) {

    private val soldOuts = ConcurrentHashMap<Int, Sector>()
    private val random = Random()

    fun play(): Sector {
        checkPlayAvailable()
        val rouletteProbability = getRouletteProbability()
        val roulette = getResult(rouletteProbability)
        decreaseStock(roulette)
        return roulette
    }

    private fun getRouletteProbability(): TreeMap<BigDecimal, Sector> {
        val remainProbability = getCurrentProbability()
        var cumulativeProbability = BigDecimal.ZERO
        val probabilityMap = TreeMap<BigDecimal, Sector>()

        this.sectorList
            .filter { r -> r.stocks.get() != 0 }
            .forEach { r ->
                val add = r.probability.add(remainProbability)
                cumulativeProbability = cumulativeProbability.add(add)
                probabilityMap[cumulativeProbability] = r
            }
        return probabilityMap
    }

    private fun getResult(sectorMap: TreeMap<BigDecimal, Sector>): Sector {
        val randomValue = BigDecimal.valueOf(random.nextDouble())
        return sectorMap.higherEntry(randomValue).value
    }

    private fun decreaseStock(sector: Sector) {
        sector.decreaseStock()
        if (sector.stocks.get() == 0) {
            soldOuts[sector.score] = sector
        }
    }

    private fun getCurrentProbability(): BigDecimal? {
        val sumSoldOutProbability = soldOuts.values
            .stream()
            .map { obj: Sector -> obj.probability }
            .reduce(BigDecimal.ZERO)
            { obj: BigDecimal, agent: BigDecimal? -> obj.add(agent) }
        val availableSize = sectorList.size - soldOuts.size
        return sumSoldOutProbability.divide(BigDecimal.valueOf(availableSize.toLong()), 3, RoundingMode.HALF_UP)
    }

    fun currentTotalStock(): Int {
        return this.sectorList
            .stream()
            .map { obj: Sector -> obj.stocks.get() }
            .reduce(0) { a: Int, b: Int -> Integer.sum(a, b) }
    }

    private fun checkPlayAvailable() {
        if (this.sectorList.size == this.soldOuts.size) {
            throw IllegalStateException()
        }
    }
}