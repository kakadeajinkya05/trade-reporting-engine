package com.aj.service;

import com.aj.entity.CurrencyType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static com.aj.entity.CurrencyType.AED;
import static com.aj.entity.CurrencyType.SAR;
import static java.lang.String.format;

public class SettlementService {

    public LocalDate adjustSettlementDate(LocalDate settlementDate, CurrencyType currencyType) {
        if (null == settlementDate || null == currencyType) {
            String requiredFieldsAreNull = format("NULL value(s) found for the requested field(s) settlementDate=%s,  currencyType=%s ", settlementDate, currencyType);
            System.err.println(requiredFieldsAreNull);
            throw new RuntimeException(requiredFieldsAreNull);
        }
        if (isSpecialCurrency(currencyType)) {
            return adjustSettlementDateFor(settlementDate, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        } else {
            return adjustSettlementDateFor(settlementDate, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY);
        }
    }

    private LocalDate adjustSettlementDateFor(LocalDate settlementDate, DayOfWeek startDayOfWeekend, DayOfWeek endDayOfWeekend, DayOfWeek adjusterDay) {
        if (settlementDate.getDayOfWeek() == startDayOfWeekend || settlementDate.getDayOfWeek() == endDayOfWeekend) {
            return settlementDate.with(TemporalAdjusters.next(adjusterDay));
        }
        return settlementDate;
    }

    private boolean isSpecialCurrency(CurrencyType currencyType) {
        return currencyType.equals(AED) || currencyType.equals(SAR);
    }
}