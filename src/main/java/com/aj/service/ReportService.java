package com.aj.service;

import com.aj.entity.Trade;
import com.aj.entity.TradeType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final List<Trade> instructions;

    public ReportService(List<Trade> instructions) {
        this.instructions = instructions;
    }

    public void generateReport() {
        Map<LocalDate, Double> incoming = new HashMap<>();
        Map<LocalDate, Double> outgoing = new HashMap<>();
        Map<String, Double> entityOutgoingRank = new HashMap<>();
        Map<String, Double> entityIncomingRank = new HashMap<>();

        SettlementService settlementService = new SettlementService();

        for (Trade instruction : instructions) {
            LocalDate settlementDate = settlementService.adjustSettlementDate(instruction.getSettlementDate(), instruction.getCurrencyType());
            double usdAmount = instruction.getUsdAmount();

            if (instruction.getTradeType() == TradeType.BUY) {
                outgoing.merge(settlementDate, usdAmount, Double::sum);
                entityOutgoingRank.merge(instruction.getEntity(), usdAmount, Double::sum);
            } else {
                incoming.merge(settlementDate, usdAmount, Double::sum);
                entityIncomingRank.merge(instruction.getEntity(), usdAmount, Double::sum);
            }
        }

        System.out.println("Daily Settled Incoming Amounts:");
        incoming.forEach((date, amount) -> System.out.println(date + ": " + amount));

        System.out.println("\nDaily Settled Outgoing Amounts:");
        outgoing.forEach((date, amount) -> System.out.println(date + ": " + amount));

        System.out.println("\nRanking of Entities Based on Outgoing Amounts:");
        entityOutgoingRank.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\nRanking of Entities Based on Incoming Amounts:");
        entityIncomingRank.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}