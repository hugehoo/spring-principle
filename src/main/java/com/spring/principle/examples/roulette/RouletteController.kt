package com.spring.principle.examples.roulette

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger

@Controller
@RequestMapping("api/roulette")
class RouletteController {
    @GetMapping("")
    private fun getRouletteInfo() {
        // 현재 업데이트된 전체 룰렛 정보를 가져와야해
        // 저장소 필요 -> redis, mysql, local
        val initRoulette = getRouletteRedis()
        val rouletteGame = RouletteGame(initRoulette)
        rouletteGame.play()
    }

    // 레디스에 저장된 룰렛은 재고가 호출할때마다 달라져야 한다. 한번 호출마다 재고 1개씩 차감
    // 즉 한번 플레이 한 후에는 레디스에 다시 갱신 박아줘야함.
    // 아이템이 6개니까 병렬로 6개 호출해서 가져오거나,
    // 아니면 한번의 호출에 모든 아이템 가져올 수 있도록 Collection 활용
    // key - value : "roulette" 이란 키에 List.of(RouletteList) 를 객체로 박아?
    // redis ->
    //  변하는 것 : 수량, 아이템별 확률,
    //  변하지 않는 것 : 아이템 점수

    // redis 에서 재고가 0 이 아닌 것만 가져오게 하려면, 매 플레이마다 재고 수량을 레디스에 갱신해서, inStocks 만 저장하면 된다.
    private fun getRouletteRedis(): List<Sector> {

        // redis 에서 재고 있는 것만 가져옴 \\/\
        // 재고 있는 것만 별도로 저장해야함.
        // 재고없는 것도 별도 저장
        // 재고 있는 것중에서 확률게임 돌려.
        // 당첨된 것 리턴 & 재고 차감
        // 0개가 되면 outofStock Redis에 저장
        // 그렇지 않으면 수량 차감 시키고 다시 저장
        // 아오 레디스 가 맞나.
        return listOf();
    }

    // rds 저장 방식
    // id score stocks probability date 컬럼
    // 매일 100개씩 플레이한다고 가정 -> 일자별로 조회


    private fun getInitRoulette(): List<Sector> {
        return listOf(
            Sector(10, BigDecimal.valueOf(0.5), AtomicInteger(40)),
            Sector(50, BigDecimal.valueOf(0.3), AtomicInteger(30)),
            Sector(100, BigDecimal.valueOf(0.1), AtomicInteger(16)),
            Sector(500, BigDecimal.valueOf(0.05), AtomicInteger(8)),
            Sector(800, BigDecimal.valueOf(0.03), AtomicInteger(4)),
            Sector(1000, BigDecimal.valueOf(0.02), AtomicInteger(2))
        )
    }

}