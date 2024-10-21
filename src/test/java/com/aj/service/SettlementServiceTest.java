package com.aj.service;

import com.aj.entity.CurrencyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.DayOfWeek.*;

class SettlementServiceTest {

    private SettlementService settlementService;

    @BeforeEach
    void setup() {
        settlementService = new SettlementService();
    }

    @Test
    void shouldAdjustSettlementDateForNormalCurrency() {
        //Instructed settlement date is FRIDAY
        LocalDate settlementDate = getParsedDate("18 Oct 2024");

        LocalDate nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.SGP);

        Assertions.assertEquals(FRIDAY, nextSettlementDate.getDayOfWeek());

        //Instructed settlement date is SATURDAY
        settlementDate = getParsedDate("19 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.SGP);

        Assertions.assertEquals(MONDAY, nextSettlementDate.getDayOfWeek());


        //Instructed settlement date is SUNDAY
        settlementDate = getParsedDate("20 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.SGP);

        Assertions.assertEquals(MONDAY, nextSettlementDate.getDayOfWeek());


        //Instructed settlement date is MONDAY
        settlementDate = getParsedDate("21 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.SGP);

        Assertions.assertEquals(MONDAY, nextSettlementDate.getDayOfWeek());

    }

    @Test
    void shouldAdjustSettlementDateForSpecialCurrency() {
        //Instructed settlement date is THURSDAY
        LocalDate settlementDate = getParsedDate("17 Oct 2024");

        LocalDate nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.AED);

        Assertions.assertEquals(THURSDAY, nextSettlementDate.getDayOfWeek());

        //Instructed settlement date is FRIDAY
        settlementDate = getParsedDate("18 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.AED);

        Assertions.assertEquals(SUNDAY, nextSettlementDate.getDayOfWeek());


        //Instructed settlement date is SATURDAY
        settlementDate = getParsedDate("19 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.AED);

        Assertions.assertEquals(SUNDAY, nextSettlementDate.getDayOfWeek());


        //Instructed settlement date is SUNDAY
        settlementDate = getParsedDate("20 Oct 2024");

        nextSettlementDate = settlementService.adjustSettlementDate(settlementDate, CurrencyType.AED);

        Assertions.assertEquals(SUNDAY, nextSettlementDate.getDayOfWeek());

    }

    private LocalDate getParsedDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
