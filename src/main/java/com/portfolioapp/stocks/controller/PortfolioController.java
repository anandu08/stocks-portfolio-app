package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortfolioController {


    private final PortfolioService portfolioService;

    @GetMapping(path = "/holdings/{userId}")
    public ResponseEntity<HoldingsResponseDTO> getHoldings(@PathVariable long userId) {
            HoldingsResponseDTO holdingsResponseDTO = portfolioService.getHoldings(userId);
            return ResponseEntity.ok(holdingsResponseDTO);

    }
    @GetMapping(path = "/portfolio/{userId}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable long userId) {
            PortfolioDTO portfolioDTO = portfolioService.getPortfolio(userId);
            return ResponseEntity.ok(portfolioDTO);

    }
}
