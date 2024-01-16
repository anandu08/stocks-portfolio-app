package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final StocksRepo stocksRepo;
    private final UserStocksRepo userStocksRepo;

    @Autowired
    public UserController(StocksRepo stocksRepo, UserStocksRepo userStocksRepo) {
        this.stocksRepo = stocksRepo;
        this.userStocksRepo = userStocksRepo;
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
        System.out.println("Type value: '" + type + "'");
        if ("buy".equalsIgnoreCase(type)) {
            buyStock(userId, stock, quantity);
            return "Stock bought successfully.";
        } else if ("sell".equalsIgnoreCase(type)) {
            sellStock(userId, stock, quantity);
            return "Stock sold successfully.";
        } else {
            return "Invalid transaction type.";
        }
    }

    private void buyStock(long userId, Stock stock, long quantity) {
        UserStocks userStocks = new UserStocks();
        UserStocksId userStocksId = new UserStocksId();
        userStocksId.setUserId(userId);
        userStocksId.setPurchasePrice(stock.getClosePrice());
        userStocksId.setStockId(stock.getId());
        userStocks.setQuantity(quantity);
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        userStocks.setStocks(stocks);
        userStocks.setId(userStocksId);

        Optional<UserStocks> userStocksOptional = userStocksRepo.findById(userStocksId);

        if (userStocksOptional.isPresent()) {


            UserStocks temp = userStocksOptional.get();
            temp.setQuantity(temp.getQuantity()+quantity);
            userStocksRepo.save(temp);
        } else {
    userStocksRepo.save(userStocks);
        }

    }

    private void sellStock(long userId, Stock stock, long quantity) {
        List<Object[]> userStockFields = userStocksRepo.findFieldsByUserIdAndStockId(userId, stock.getId());

        for (Object[] fields : userStockFields) {
            long boughtQuantity = (long) fields[0];
            BigDecimal purchasedPrice = (BigDecimal) fields[1];

            if (quantity >= boughtQuantity) {
                userStocksRepo.deleteByUserIdAndStockIdAndPurchasePrice(userId, stock.getId(), purchasedPrice);
                quantity -= boughtQuantity;
            } else {
                boughtQuantity -= quantity;
                quantity = 0L;
                userStocksRepo.updateQuantityByUserIdAndStockIdAndPurchasePrice(userId, stock.getId(), purchasedPrice, boughtQuantity);
            }

            System.out.println("Stock sold successfully. Updated Quantity: " + quantity);
            System.out.println("Purchase Price: " + purchasedPrice);

            if (quantity == 0) {
                break;
            }
        }
    }
}