package com.aj.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ReportServiceTest {

    private ReportService reportService;

    @BeforeEach
    void setup() {
        reportService = new ReportService(List.of());
    }

    @Test
    void shouldAdjustSettlementDateForNormalCurrency() {

    }
}
