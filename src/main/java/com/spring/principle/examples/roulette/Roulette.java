package com.spring.principle.examples.roulette;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Roulette {
    private Integer score;
    private BigDecimal probability;
    private Integer stocks;

    public void decreaseStock() {
        if (stocks > 0) {
            stocks -= 1;
        }
    }
}
