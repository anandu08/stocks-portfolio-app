package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.model.AvgPrice;
import com.portfolioapp.stocks.model.AvgPriceId;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.AvgPriceRepo;
import com.portfolioapp.stocks.service.AvgPriceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AvgPriceServiceImpl implements AvgPriceService {

    @Autowired
    private AvgPriceRepo avgPriceRepo;

    @Override
    public void updateAvgPrice(long userId, Stock stock, long newQuantity) {

        AvgPriceId avgPriceId = new AvgPriceId();
        avgPriceId.setStockId(stock.getId());
        avgPriceId.setUserId(userId);

        AvgPrice avgPrice = avgPriceRepo.findById(avgPriceId)
                .orElseThrow(() -> new EntityNotFoundException("AvgPrice not found with id: " + avgPriceId));

        long currentQuantity = avgPrice.getQuantity();
        BigDecimal currentAvgPrice = avgPrice.getAvgPrice();


        BigDecimal totalValue = currentAvgPrice.multiply(BigDecimal.valueOf(currentQuantity));

        totalValue = totalValue.add(stock.getClosePrice().multiply(BigDecimal.valueOf(newQuantity)));

        newQuantity += currentQuantity;

        BigDecimal newAvgPrice = totalValue.divide(BigDecimal.valueOf(newQuantity), 2, RoundingMode.HALF_UP);


        avgPrice.setQuantity(newQuantity);
        avgPrice.setAvgPrice(newAvgPrice);

        avgPriceRepo.save(avgPrice);
    }



}
