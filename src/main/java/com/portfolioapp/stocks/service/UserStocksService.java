package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.UserStocksId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public interface UserStocksService {

    void updateQuantity(UserStocksId userStocksId, long newQuantity);
}
