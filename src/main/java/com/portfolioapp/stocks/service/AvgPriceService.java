package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;
import org.springframework.stereotype.Service;

@Service
public interface AvgPriceService {

    void updateAvgPrice(long userId, Stock stock,long newQuantity);
}
