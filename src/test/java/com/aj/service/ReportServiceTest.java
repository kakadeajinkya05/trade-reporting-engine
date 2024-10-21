package com.aj.service;

import com.aj.entity.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.aj.entity.CurrencyType.*;
import static com.aj.entity.TradeType.BUY;
import static com.aj.entity.TradeType.SELL;

class ReportServiceTest {

    private ReportService reportService;
    private List<Trade> trades;

    @BeforeEach
    void setup() {
        trades = getTradeInstructions();
    }

    @Test
    void shouldThrowAnExceptionForInvalidTradeType() {
        Trade trade = new Trade("foo", null, 0.50, AED, getParsedDate("01 Oct 2024"), getParsedDate("01 Oct 2024"), 200, 100.25);
        trades.add(trade);
        reportService = new ReportService(trades);

        Assertions.assertThrows(RuntimeException.class, () -> reportService.generateReport());
    }

    @Test
    void shouldGenerateReportForMixedCases() {
        //Case: 1
        Trade trade = new Trade("foo", BUY, 0.0, AED, getParsedDate("01 Oct 2024"), getParsedDate("01 Oct 2024"), 0, 1);
        reportService = new ReportService(List.of(trade));

        reportService.generateReport();

        Assertions.assertEquals(0, trade.getUsdAmount());

        //Case: 2
        trade = new Trade("foo", BUY, 0.0, AED, getParsedDate("01 Oct 2024"), getParsedDate("01 Oct 2024"), 1, 1);
        reportService = new ReportService(List.of(trade));

        reportService.generateReport();

        Assertions.assertEquals(1, trade.getUsdAmount());

        //Case: 3
        trade = new Trade("foo", BUY, 0.56, AED, getParsedDate("01 Oct 2024"), getParsedDate("02 Oct 2024"), 2, 1);
        reportService = new ReportService(List.of(trade));

        reportService.generateReport();

        Assertions.assertEquals(1.12, trade.getUsdAmount());

        //Case: 4
        trade = new Trade("foo", BUY, 0.56, AED, getParsedDate("01 Oct 2024"), getParsedDate("03 Oct 2024"), 5, 0);
        reportService = new ReportService(List.of(trade));

        reportService.generateReport();

        Assertions.assertEquals(0.0, trade.getUsdAmount());

        //Case: 5
        trade = new Trade("foo", BUY, 0.56, AED, getParsedDate("01 Oct 2024"), getParsedDate("04 Oct 2024"), 5, 7.65);
        reportService = new ReportService(List.of(trade));

        reportService.generateReport();

        Assertions.assertEquals(21.42, trade.getUsdAmount());
    }

    @Test
    void shouldGenerateReport() {
        reportService = new ReportService(trades);

        reportService.generateReport();

        Assertions.assertEquals(15400.32, reportService.getEntityOutgoingRank().get("foo3").doubleValue());

        Assertions.assertEquals(6870.400000000001, reportService.getEntityIncomingRank().get("bar4").doubleValue());

        Assertions.assertEquals(13638.139500000001, reportService.getIncoming().get(getParsedDate("21 Oct 2024")).doubleValue());
        Assertions.assertEquals(4950.0, reportService.getIncoming().get(getParsedDate("18 Oct 2024")).doubleValue());

        Assertions.assertEquals(20066.452, reportService.getOutgoing().get(getParsedDate("20 Oct 2024")).doubleValue());
        Assertions.assertEquals(10025.0, reportService.getOutgoing().get(getParsedDate("17 Oct 2024")).doubleValue());

        Assertions.assertEquals(8, reportService.getInstructions().size());
    }

    private List<Trade> getTradeInstructions() {
        ArrayList<Trade> instructions = new ArrayList<>();
        Trade trade1 = new Trade("foo1", BUY, 0.50, AED, getParsedDate("01 Oct 2024"), getParsedDate("17 Oct 2024"), 200, 100.25);
        Trade trade2 = new Trade("foo2", BUY, 0.10, AED, getParsedDate("01 Oct 2024"), getParsedDate("18 Oct 2024"), 680, 10.39);
        Trade trade3 = new Trade("foo3", BUY, 0.80, AED, getParsedDate("01 Oct 2024"), getParsedDate("19 Oct 2024"), 120, 160.42);
        Trade trade4 = new Trade("foo4", BUY, 0.43, SAR, getParsedDate("01 Oct 2024"), getParsedDate("20 Oct 2024"), 20, 460.42);

        Trade trade5 = new Trade("bar1", SELL, 0.22, SGP, getParsedDate("05 Oct 2024"), getParsedDate("18 Oct 2024"), 150, 150.0);
        Trade trade6 = new Trade("bar2", SELL, 0.25, SGP, getParsedDate("05 Oct 2024"), getParsedDate("19 Oct 2024"), 193, 63.23);
        Trade trade7 = new Trade("bar3", SELL, 0.23, SGP, getParsedDate("06 Oct 2024"), getParsedDate("20 Oct 2024"), 201, 80.4);
        Trade trade8 = new Trade("bar4", SELL, 0.0, USD, getParsedDate("06 Oct 2024"), getParsedDate("21 Oct 2024"), 76, 90.4);

        instructions.add(trade1);
        instructions.add(trade2);
        instructions.add(trade3);
        instructions.add(trade4);
        instructions.add(trade5);
        instructions.add(trade6);
        instructions.add(trade7);
        instructions.add(trade8);

        return instructions;
    }

    private LocalDate getParsedDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
