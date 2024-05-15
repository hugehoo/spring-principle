package com.spring.principle.examples.roulette

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

class RouletteGame(val sectorList: List<Sector>) {

<<<<<<< Updated upstream
    private val soldOuts = ConcurrentHashMap<Int, Sector>()
=======
<<<<<<< Updated upstream
    private val soldOuts = ConcurrentHashMap<Int, Roulette>()
>>>>>>> Stashed changes
    private val random = Random()
=======
//    private val soldOuts = HashMap<Int, Sector>()
    private var soldOuts = mutableListOf<Sector>()
>>>>>>> Stashed changes

    fun play(): Sector {
        checkPlayAvailable()
        val rouletteProbability = getRouletteProbability()
        val roulette = getResult(rouletteProbability)
        decreaseStock(roulette)
        return roulette
    }

<<<<<<< Updated upstream
    private fun getRouletteProbability(): TreeMap<BigDecimal, Sector> {
=======
<<<<<<< Updated upstream
    private fun getRouletteProbability(): TreeMap<BigDecimal, Roulette> {
>>>>>>> Stashed changes
        val remainProbability = getCurrentProbability()
=======
    private fun getRouletteProbability(): TreeMap<BigDecimal, Sector> {
        val rebalancedProbability = getRebalancedProbability()
>>>>>>> Stashed changes
        var cumulativeProbability = BigDecimal.ZERO
        val probabilityMap = TreeMap<BigDecimal, Sector>()

<<<<<<< Updated upstream
        this.sectorList
=======
<<<<<<< Updated upstream
        this.rouletteList
>>>>>>> Stashed changes
            .filter { r -> r.stocks.get() != 0 }
            .forEach { r ->
                val add = r.probability.add(remainProbability)
=======
        this.sectorList
            .filter { sector -> sector.stocks.get() != 0 }
            .forEach { sector ->
                val add = sector.probability.add(rebalancedProbability)
>>>>>>> Stashed changes
                cumulativeProbability = cumulativeProbability.add(add)
                probabilityMap[cumulativeProbability] = sector
            }
        return probabilityMap
    }

<<<<<<< Updated upstream
    private fun getResult(sectorMap: TreeMap<BigDecimal, Sector>): Sector {
=======
<<<<<<< Updated upstream
    private fun getResult(rouletteMap: TreeMap<BigDecimal, Roulette>): Roulette {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
            .map { obj: Sector -> obj.probability }
=======
            .map { obj: Roulette -> obj.probability }
=======
    private fun getRebalancedProbability(): BigDecimal? {
        val sumSoldOutProbability = soldOuts.stream()
            .map { obj: Sector -> obj.probability }
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            .reduce(BigDecimal.ZERO)
            { obj: BigDecimal, agent: BigDecimal? -> obj.add(agent) }
        val availableSize = sectorList.size - soldOuts.size
        return sumSoldOutProbability.divide(BigDecimal.valueOf(availableSize.toLong()), 3, RoundingMode.HALF_UP)
    }

    private fun decreaseStock(sector: Sector) {
        sector.decreaseStock()
        if (sector.stocks.get() == 0) {
            soldOuts.add(sector)
//            soldOuts[sector.score] = sector
        }
    }

    fun currentTotalStock(): Int {
        return this.sectorList
            .stream()
            .map { obj: Sector -> obj.stocks.get() }
            .reduce(0) { a: Int, b: Int -> Integer.sum(a, b) }
    }

    private fun getResult(sectorMap: TreeMap<BigDecimal, Sector>): Sector {
        val random = Random()
        val randomValue = BigDecimal.valueOf(random.nextDouble())
        return sectorMap.higherEntry(randomValue).value
    }

    private fun checkPlayAvailable() {
        if (this.sectorList.size == this.soldOuts.size) {
            throw IllegalStateException()
        }
    }
}