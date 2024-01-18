package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.dto.HoldingsDTO;
import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.TransactionRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PortfolioController {

    @Autowired
    private StocksRepo stocksRepo;
    @Autowired
    private UserStocksRepo userStocksRepo;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private UserStocksService userStocksService;


    @GetMapping(path = "/holdings/{userId}")
    public HoldingsResponseDTO getHoldings(@PathVariable long userId) {


        List<StockSummaryDTO> rows = userStocksRepo.getStockSummariesByUserId(userId);
        HoldingsResponseDTO holdingsResponseDTO = new HoldingsResponseDTO();
        List<HoldingsDTO> stockHoldingsList = new ArrayList<>();

        BigDecimal totalHoldings = BigDecimal.ZERO;
        for (StockSummaryDTO row : rows) {
            String stockId = row.getStockId();
            Stock stock = stocksRepo.getReferenceById(stockId);
            BigDecimal stockHoldings = stock.getClosePrice().multiply(BigDecimal.valueOf(row.getTotalQuantity()));
            BigDecimal avgPrice = userStocksService.findAvgPrice(userId, stockId);
            BigDecimal GainOrLoss = BigDecimal.ZERO;
            GainOrLoss = stock.getClosePrice().subtract(avgPrice).multiply(BigDecimal.valueOf(row.getTotalQuantity()));
            GainOrLoss = GainOrLoss.setScale(2, RoundingMode.HALF_UP);


            HoldingsDTO holdingsDTO = HoldingsDTO.builder()
                    .stockName(stock.getStockName())
                    .quantity(row.getTotalQuantity())
                    .userId(userId)
                    .stockId(stockId)
                    .StockHoldings(stockHoldings)
                    .GainOrLoss(GainOrLoss)
                    .AvgBuyPrice(avgPrice)
                    .build();

            totalHoldings = totalHoldings.add(stockHoldings);
            stockHoldingsList.add(holdingsDTO);


        }
        holdingsResponseDTO.setHoldings(stockHoldingsList);
        holdingsResponseDTO.setTotalHoldings(totalHoldings);


        return holdingsResponseDTO;
    }


    @GetMapping(path = "/portfolio/{userId}")
    public PortfolioDTO getPorfolio(@PathVariable long userId) {

        List<Transactions> transactions = transactionRepo.findByUserId(userId);

        HoldingsResponseDTO holdingsResponseDTO = getHoldings(userId);

        BigDecimal totalBuy = BigDecimal.ZERO;
        long BuyQty = 0;

        BigDecimal totalSell = BigDecimal.ZERO;
        long SellQty = 0;

        BigDecimal GainOrLoss = BigDecimal.ZERO;
        BigDecimal GainOrLossPercent = BigDecimal.ZERO;

        for (Transactions transaction : transactions) {
            if ("buy".equals(transaction.getType())) {
                totalBuy = totalBuy.add(transaction.getTransactPrice());
                BuyQty += transaction.getQuantity();
            }
            if ("sell".equals(transaction.getType())) {
                totalSell = totalSell.add(transaction.getTransactPrice());
                SellQty += transaction.getQuantity();
            }
        }


        if (SellQty != 0 && BuyQty != 0) {
            BigDecimal avgSell = totalSell.divide(BigDecimal.valueOf(SellQty), 2, RoundingMode.HALF_UP);
            BigDecimal avgBuy = totalBuy.divide(BigDecimal.valueOf(BuyQty), 2, RoundingMode.HALF_UP);

            GainOrLoss = avgSell.subtract(avgBuy).multiply(BigDecimal.valueOf(SellQty));
            GainOrLossPercent = GainOrLoss.divide(avgBuy, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        return PortfolioDTO.builder()
                .totalBuyPrice(totalBuy)
                .totalPnL(GainOrLoss)
                .totalPnLpercent(GainOrLossPercent)
                .holdingsResponseDTO(holdingsResponseDTO)
                .build();
    }
}