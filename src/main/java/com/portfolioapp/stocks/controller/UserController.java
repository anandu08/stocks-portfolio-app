package com.portfolioapp.stocks.controller;


import com.portfolioapp.stocks.exception.InvalidQuantityException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.service.StocksService;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
@RestController
public class UserController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserStocksService userStocksService;
    @Autowired
    private StocksService stocksService;
    @PostMapping(path = "/transact/{userId}")
    public String buyOrSell(@PathVariable long userId,
                            @RequestBody Map<String, Object> transactionDetails) {
        try {
            String type = (String) transactionDetails.get("type");
            long stockId = ((Number) transactionDetails.get("stockId")).longValue();
            long quantity = ((Number) transactionDetails.get("quantity")).longValue();

            Stock stock = stocksService.findStockById(stockId);

            if (stock == null) {
                throw new StockNotFoundException("Stock not found.");
            }

            if (quantity <= 0) {
                throw new InvalidQuantityException("Invalid quantity. Quantity must be greater than 0.");
            }

            if ("buy".equalsIgnoreCase(type)) {
                transactionService.buyStock(userId, stock, quantity);
                return "Stock bought successfully.";
            } else if ("sell".equalsIgnoreCase(type)) {
                if (transactionService.sellStock(userId, stock, quantity)) {
                    return "Stock sold successfully.";
                } else {
                    throw new StockNotAvailableException("No such stock.");
                }
            } else {
                throw new IllegalArgumentException("Invalid transaction type.");
            }
        } catch (StockNotFoundException | InvalidQuantityException | StockNotAvailableException e) {
            return e.getMessage();
        }
    catch (Exception e) {
        return "An unexpected error occurred.";
    }

    }
}
