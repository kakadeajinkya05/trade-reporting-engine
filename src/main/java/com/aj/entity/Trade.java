package com.aj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
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
        double result = 1;

        if (units == 0) {
            return 0.0;
        } else {
            result *= units;
        }

        if (pricePerUnit != 0) {
            result *= pricePerUnit;
        } else {
            result *= pricePerUnit;
            return result;
        }

        if (agreedFx != 0) {
            result *= agreedFx;
        }

        return result;
    }

}
