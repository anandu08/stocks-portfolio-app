package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.exception.InvalidQuantityException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.TransactionRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserStocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final StocksRepo stocksRepo;
    private final UserStocksRepo userStocksRepo;
    private final UserStocksService userStocksService;

    private final TransactionRepo transactionRepo;

@Override
    public void buyStock(long userId, Stock stock, long quantity) {


    if (stock == null) {
        throw new StockNotFoundException("Stock not found.");
    }
    if (quantity <= 0) {
        throw new InvalidQuantityException("Invalid quantity. Quantity must be greater than 0.");
    }
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
        transactionRepo.save(transactions);

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
    public List<Transactions> findByUserId(Long userId) {
        return transactionRepo.findByUserId(userId);
    }

    @Override
    public boolean sellStock(long userId, Stock stock, long quantity) {

        Transactions transactions = new Transactions();


        List<UserStocks> userStockFields = userStocksRepo.findByUserIdAndStockId(userId, stock.getId());
    if (userStockFields.isEmpty()) {
        throw new StockNotAvailableException("No stocks available for selling.");
    }

        UserStocksId userStocksId = new UserStocksId();
        userStocksId.setStockId(stock.getId());
        userStocksId.setUserId(userId);

        transactions.setUserId(userId);
        transactions.setType("sell");
        transactions.setStockId(stock.getId());
        transactions.setTransactPrice(stock.getClosePrice());
        transactions.setQuantity(quantity);

        transactionRepo.save(transactions);

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
    return true;
}
}
