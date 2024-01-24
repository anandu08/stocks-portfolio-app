package com.portfolioapp.stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockSummaryDTO {
    private Long stockId;
    private Long totalQuantity;
}
