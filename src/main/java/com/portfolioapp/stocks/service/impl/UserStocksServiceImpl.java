package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.exception.InvalidQuantityException;
import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class UserStocksServiceImpl implements UserStocksService {

    @Autowired
    private UserStocksRepo userStocksRepo;

    @Override
    public void updateQuantity(UserStocksId userStocksId, long newQuantity) {
        Optional<UserStocks> userStocksOptional = userStocksRepo.findById(userStocksId);

        if(newQuantity<0)
            throw new InvalidQuantityException(newQuantity);
        if (userStocksOptional.isPresent()) {
            UserStocks userStocks = userStocksOptional.get();
            userStocks.setQuantity(newQuantity);
            userStocksRepo.save(userStocks);
        } else {
            throw new RuntimeException("No Stock data found for id: " + userStocksId);
        }
    }

    @Override
    public BigDecimal findAvgPrice(long userId, Long stockId) {
        List<UserStocks> purchasePrices = userStocksRepo.findByUserIdAndStockId(userId, stockId);

        if(purchasePrices.isEmpty())
            throw new RuntimeException(String.format("No data found for userId: %d and StockId: %d", userId, stockId));
        long totalQty = 0;
        BigDecimal totalValue = BigDecimal.ZERO;

        for (UserStocks userStock : purchasePrices) {
            totalQty += userStock.getQuantity();
            totalValue = totalValue.add(userStock.getId().getPurchasePrice().multiply(BigDecimal.valueOf(userStock.getQuantity())));
        }

        if (totalQty != 0) {
            return totalValue.divide(BigDecimal.valueOf(totalQty), 2, RoundingMode.HALF_UP);
        } else {
            throw new UserNotFoundException("User or stock not found for userId: " + userId + ", stockId: " + stockId);
        }
    }

    @Override
    public List<StockSummaryDTO> getStockSummariesByUserId(Long userId) {
        List<StockSummaryDTO> stockSummaryDTOList= userStocksRepo.getStockSummariesByUserId(userId);
        if(stockSummaryDTOList.isEmpty())
            throw new UserNotFoundException("No data found for userId:"+userId);
        return stockSummaryDTOList;
    }
}
