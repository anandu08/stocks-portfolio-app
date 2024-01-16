package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.UserStocksService;
import com.portfolioapp.stocks.utils.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class UserController {

    private final StocksRepo stocksRepo;

    private final Transactions transactions;

    @Autowired
    public UserController(StocksRepo stocksRepo, Transactions transactions) {
        this.stocksRepo = stocksRepo;
        this.transactions = transactions;
    }

    @PostMapping(path = "/transact/{userId}")
    public String buyOrSell(@PathVariable long userId,
                            @RequestBody Map<String, Object> transactionDetails) {

        String type = (String) transactionDetails.get("type");
        String stockId = (String) transactionDetails.get("stockId");
        long quantity = Long.parseLong((String) transactionDetails.get("quantity"));

        Stock stock = stocksRepo.findStockById(stockId);

        if (stock == null) {
            return "Stock not found.";
        }

        if (quantity <= 0) {
            return "Invalid quantity. Quantity must be greater than 0.";
        }

        if ("buy".equalsIgnoreCase(type)) {

            transactions.buyStock(userId, stock, quantity);

            return "Stock bought successfully.";
        }

        else if ("sell".equalsIgnoreCase(type)) {

            transactions.sellStock(userId, stock, quantity);

            return "Stock sold successfully.";
        }

        else {

            return "Invalid transaction type.";
        }
    }




}