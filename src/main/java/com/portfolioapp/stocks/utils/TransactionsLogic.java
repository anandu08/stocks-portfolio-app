package com.portfolioapp.stocks.utils;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class TransactionsLogic {
    @Autowired
    private StocksRepo stocksRepo;
    @Autowired
    private UserStocksRepo userStocksRepo;
    @Autowired
    private UserStocksService userStocksService;


    public void buyStock(long userId, Stock stock, long quantity) {

        UserStocks userStocks = new UserStocks();
        UserStocksId userStocksId = new UserStocksId();
        Transactions transactions = new Transactions();


        userStocksId.setUserId(userId);
        userStocksId.setPurchasePrice(stock.getClosePrice());
        userStocksId.setStockId(stock.getId());

        userStocks.setId(userStocksId);
        userStocks.setQuantity(quantity);
        Set<Stock> stocks = new HashSet<>();
        stocks.add(stock);
        userStocks.setStocks(stocks);


        transactions.setUserId(userId);
        transactions.setType("buy");
        transactions.setStockId(stock.getId());
        transactions.setTransactPrice(stock.getClosePrice());
        transactions.setQuantity(quantity);

        Optional<UserStocks> userStocksOptional = userStocksRepo.findById(userStocksId);

        if (userStocksOptional.isPresent()) {


            UserStocks temp = userStocksOptional.get();
            temp.setQuantity(temp.getQuantity()+quantity);
            userStocksRepo.save(temp);
        } else {
            userStocksRepo.save(userStocks);
        }

    }

    public void sellStock(long userId, Stock stock, long quantity) {

        Transactions transactions = new Transactions();


        List<Object[]> userStockFields = userStocksRepo.findFieldsByUserIdAndStockId(userId, stock.getId());

        UserStocksId userStocksId = new UserStocksId();
        userStocksId.setStockId(stock.getId());
        userStocksId.setUserId(userId);

        transactions.setUserId(userId);
        transactions.setType("sell");
        transactions.setStockId(stock.getId());
        transactions.setTransactPrice(stock.getClosePrice());
        transactions.setQuantity(quantity);

        System.out.println(userStockFields.size());

        for (Object[] fields : userStockFields) {

            long boughtQuantity = (long) fields[0];
            BigDecimal purchasedPrice = (BigDecimal) fields[1];

            System.out.println(purchasedPrice);

            userStocksId.setPurchasePrice(purchasedPrice);

            if (quantity >= boughtQuantity) {
                userStocksRepo.deleteById(userStocksId);
                quantity -= boughtQuantity;
            } else {
                boughtQuantity -= quantity;
                quantity = 0L;
                userStocksService.updateQuantity(userStocksId, boughtQuantity);
            }

            System.out.println("Stock sold successfully. Updated Quantity: " + quantity);
            System.out.println("Purchase Price: " + purchasedPrice);

            if (quantity == 0) {
                break;
            }
        }
    }
}
