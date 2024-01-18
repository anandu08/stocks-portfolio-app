package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.utils.TransactionsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    @Autowired
    private final StocksRepo stocksRepo;

    @Autowired
    private final TransactionService transactionService;

    public UserController(StocksRepo stocksRepo, TransactionService transactionService) {
        this.stocksRepo = stocksRepo;
        this.transactionService = transactionService;

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

            transactionService.buyStock(userId, stock, quantity);

            return "Stock bought successfully.";
        }

        else if ("sell".equalsIgnoreCase(type)) {

            transactionService.sellStock(userId, stock, quantity);

            return "Stock sold successfully.";
        }

        else {

            return "Invalid transaction type.";
        }
    }




}