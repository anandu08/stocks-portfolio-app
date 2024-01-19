package com.portfolioapp.stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockSummaryDTO {
    private Long stockId;
    private Long totalQuantity;
}
