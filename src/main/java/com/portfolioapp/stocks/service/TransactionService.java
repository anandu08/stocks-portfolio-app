package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;
import org.springframework.stereotype.Service;


@Service
public interface TransactionService {

    public boolean sellStock(long userId, Stock stock, long quantity);
    public void buyStock(long userId, Stock stock, long quantity);
}
