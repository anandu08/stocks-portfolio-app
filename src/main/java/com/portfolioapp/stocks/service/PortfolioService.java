package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import org.springframework.stereotype.Service;


@Service
public interface PortfolioService {


    HoldingsResponseDTO getHoldings(long userId) ;

    public PortfolioDTO getPortfolio(long userId);

    }
