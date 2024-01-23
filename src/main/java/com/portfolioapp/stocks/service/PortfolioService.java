package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.exception.DataNotFoundException;
import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.exception.UserNotFoundException;
import org.springframework.stereotype.Service;


@Service
public interface PortfolioService {


    HoldingsResponseDTO getHoldings(long userId) throws StockNotFoundException, UserNotFoundException, DataNotFoundException ;

    public PortfolioDTO getPortfolio(long userId) throws StockNotFoundException, UserNotFoundException, DataNotFoundException;

    }
