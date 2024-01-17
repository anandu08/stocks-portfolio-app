package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.utils.TransactionsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    private final StocksRepo stocksRepo;

    private final TransactionsLogic transactionsLogic;

    @Autowired
    public UserController(StocksRepo stocksRepo, TransactionsLogic transactionsLogic) {
        this.stocksRepo = stocksRepo;
        this.transactionsLogic = transactionsLogic;
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

            transactionsLogic.buyStock(userId, stock, quantity);

            return "Stock bought successfully.";
        }

        else if ("sell".equalsIgnoreCase(type)) {

            transactionsLogic.sellStock(userId, stock, quantity);

            return "Stock sold successfully.";
        }

        else {

            return "Invalid transaction type.";
        }
    }




}