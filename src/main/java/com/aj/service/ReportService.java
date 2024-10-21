package com.aj.service;

import com.aj.entity.Trade;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.DoubleAdder;

import static com.aj.entity.TradeType.BUY;
import static com.aj.entity.TradeType.SELL;
import static java.lang.String.format;

@Getter
public class ReportService {

    private final List<Trade> instructions;
    private Map<LocalDate, DoubleAdder> incoming = new ConcurrentHashMap<>();
    private Map<LocalDate, DoubleAdder> outgoing = new ConcurrentHashMap<>();
    private Map<String, DoubleAdder> entityOutgoingRank = new ConcurrentHashMap<>();
    private Map<String, DoubleAdder> entityIncomingRank = new ConcurrentHashMap<>();

    public ReportService(List<Trade> instructions) {
        this.instructions = new CopyOnWriteArrayList<>(instructions);
    }

    public void generateReport() {
        SettlementService settlementService = new SettlementService();
        for (Trade instruction : instructions) {
            if (instruction.getTradeType() == BUY) {
                executeInstruction(settlementService, instruction, outgoing, entityOutgoingRank);
            } else if (instruction.getTradeType() == SELL) {
                executeInstruction(settlementService, instruction, incoming, entityIncomingRank);
            } else {
                String requiredFieldsAreNull = format("invalid value found for the requested field TradeType=%s", instruction.getTradeType());
                System.err.println(requiredFieldsAreNull);
                throw new RuntimeException(requiredFieldsAreNull);
            }
        }

        System.out.println("\nDaily Settled Incoming Amounts:");
        incoming.forEach((date, amount) -> System.out.println(date + ": " + amount));

        System.out.println("\nDaily Settled Outgoing Amounts:");
        outgoing.forEach((date, amount) -> System.out.println(date + ": " + amount));

        System.out.println("\nRanking of Entities Based on Outgoing Amounts:");
        entityOutgoingRank.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue().doubleValue(), e1.getValue().doubleValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\nRanking of Entities Based on Incoming Amounts:");
        entityIncomingRank.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue().doubleValue(), e1.getValue().doubleValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    void executeInstruction(SettlementService settlementService, Trade instruction, Map<LocalDate, DoubleAdder> map, Map<String, DoubleAdder> rank) {
        LocalDate settlementDate = settlementService.adjustSettlementDate(instruction.getSettlementDate(), instruction.getCurrencyType());
        double usdAmount = instruction.getUsdAmount();
        map.computeIfAbsent(settlementDate, k -> new DoubleAdder()).add(usdAmount);
        rank.computeIfAbsent(instruction.getEntity(), k -> new DoubleAdder()).add(usdAmount);
    }
}