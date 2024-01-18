package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;

public interface AvgPriceService {

    void updateAvgPrice(long userId, Stock stock,long newQuantity);
}
