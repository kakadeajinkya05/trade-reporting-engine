package com.aj.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    private String entity;
    private TradeType tradeType;
    private double agreedFx;
    private CurrencyType currencyType;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private int units;
    private double pricePerUnit;

    public double getUsdAmount() {
        return pricePerUnit * units * agreedFx;
    }

}
