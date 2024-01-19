package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.dto.HoldingsDTO;
import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.service.StocksService;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private TransactionService transactionService;
    @Autowired
    private UserStocksService userStocksService;
    @Autowired
    private StocksService stocksService;


    @GetMapping(path = "/holdings/{userId}")
    public ResponseEntity<HoldingsResponseDTO> getHoldings(@PathVariable long userId) {


        try{
            List<StockSummaryDTO> rows = userStocksService.getStockSummariesByUserId(userId);
            if (rows.isEmpty() )
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HoldingsResponseDTO());
            HoldingsResponseDTO holdingsResponseDTO = new HoldingsResponseDTO();
            List<HoldingsDTO> stockHoldingsList = new ArrayList<>();

            BigDecimal totalHoldings = BigDecimal.ZERO;
            for (StockSummaryDTO row : rows) {
                long stockId = row.getStockId();
                Stock stock = stocksService.getReferenceById(stockId);
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


            return ResponseEntity.ok(holdingsResponseDTO);
        }
        catch (StockNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HoldingsResponseDTO());
        }
    }


    @GetMapping(path = "/portfolio/{userId}")
    public ResponseEntity<PortfolioDTO> getPorfolio(@PathVariable long userId) {

        try{
            List<Transactions> transactions = transactionService.findByUserId(userId);
            if (transactions.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PortfolioDTO());
            HoldingsResponseDTO holdingsResponseDTO = getHoldings(userId).getBody();

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

            PortfolioDTO portfolioDTO = PortfolioDTO.builder()
                    .totalBuyPrice(totalBuy)
                    .totalPnL(GainOrLoss)
                    .totalPnLpercent(GainOrLossPercent)
                    .holdingsResponseDTO(holdingsResponseDTO)
                    .build();

            return ResponseEntity.ok(portfolioDTO);
        }
        catch (StockNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PortfolioDTO.builder().build());
        }
    }
}