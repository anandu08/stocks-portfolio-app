package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.model.UserStocksId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public interface UserStocksService {

    void updateQuantity(UserStocksId userStocksId, long newQuantity);
    BigDecimal findAvgPrice(long userId,Long stockId);
    List<StockSummaryDTO> getStockSummariesByUserId(Long userId);
}
