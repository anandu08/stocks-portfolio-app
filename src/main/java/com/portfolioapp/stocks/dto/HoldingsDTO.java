package com.portfolioapp.stocks.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HoldingsDTO {

    private String stockId;
    private String stockName;
    private Long userId;
    private Long quantity;
    private BigDecimal StockHoldings;
    private BigDecimal AvgBuyPrice;
    private BigDecimal GainOrLoss;

}
