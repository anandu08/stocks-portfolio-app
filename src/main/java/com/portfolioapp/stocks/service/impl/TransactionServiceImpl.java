package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.TransactionRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private StocksRepo stocksRepo;
    @Autowired
    private UserStocksRepo userStocksRepo;
    @Autowired
    private UserStocksService userStocksService;

    @Autowired
    private TransactionRepo transactionRepo;

@Override
    public void buyStock(long userId, Stock stock, long quantity) {

        UserStocks userStocks = new UserStocks();
        UserStocksId userStocksId = new UserStocksId();
        Transactions transactions = new Transactions();


        userStocksId.setUserId(userId);
        userStocksId.setPurchasePrice(stock.getClosePrice());
        userStocksId.setStockId(stock.getId());

        userStocks.setId(userStocksId);
        userStocks.setQuantity(quantity);



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
@Override
    public void sellStock(long userId, Stock stock, long quantity) {

        Transactions transactions = new Transactions();


        List<UserStocks> userStockFields = userStocksRepo.findByUserIdAndStockId(userId, stock.getId());

        UserStocksId userStocksId = new UserStocksId();
        userStocksId.setStockId(stock.getId());
        userStocksId.setUserId(userId);

        transactions.setUserId(userId);
        transactions.setType("sell");
        transactions.setStockId(stock.getId());
        transactions.setTransactPrice(stock.getClosePrice());
        transactions.setQuantity(quantity);

        System.out.println(userStockFields.size());

        for (UserStocks fields : userStockFields) {

            long boughtQuantity =  fields.getQuantity();
            BigDecimal purchasedPrice =  fields.getId().getPurchasePrice();

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
