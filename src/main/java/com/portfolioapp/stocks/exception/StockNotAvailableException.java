package com.portfolioapp.stocks.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor

public class StockNotAvailableException extends RuntimeException {
    public StockNotAvailableException(long stockId) {
        super("Stock not available for id: "+stockId);
    }
}