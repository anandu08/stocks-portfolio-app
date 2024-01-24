package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface TransactionService {

    public void sellStock(long userId, Stock stock, long quantity);
    public void buyStock(long userId, Stock stock, long quantity);
    List<Transactions>   findByUserId(Long userId);

}
