package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserStocksServiceImpl implements UserStocksService {

    @Autowired
    UserStocksRepo userStocksRepo;
    @Override
    public void updateQuantity(UserStocksId userStocksId, long newQuantity) {
        Optional<UserStocks> userStocksOptional = userStocksRepo.findById(userStocksId);

        userStocksOptional.ifPresent(userStocks -> {
            userStocks.setQuantity(newQuantity);
            userStocksRepo.save(userStocks);
        });
    }
}
