package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.dto.HoldingsDTO;
import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.dto.StockSummaryDTO;

import com.portfolioapp.stocks.exception.TransactionNotFoundException;
import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {


    private final TransactionService transactionService;

    private final UserStocksService userStocksService;
    private final StocksService stocksService;
    private final UserService userService;

    @Override
    public HoldingsResponseDTO getHoldings(long userId){

        Optional<User> user = userService.findUserById(userId);

            if (user.isEmpty()) {
                throw new UserNotFoundException("No data found for userId:"+userId);
            }
            List<StockSummaryDTO> rows = userStocksService.getStockSummariesByUserId(userId);
            if (rows.isEmpty()) {
                throw new UserNotFoundException("No data found for userId:"+userId);
            }

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


            return holdingsResponseDTO;
        }



    @Override
    public PortfolioDTO getPortfolio(long userId) {


            Optional<User> user = userService.findUserById(userId);
            if (user.isEmpty()) {
                throw new UserNotFoundException("No user found with Id:"+userId);
            }

            List<Transactions> transactions = transactionService.findByUserId(userId);
            if (transactions.isEmpty()) {
                throw new TransactionNotFoundException(userId);
            }

            HoldingsResponseDTO holdingsResponseDTO = getHoldings(userId);

            BigDecimal totalBuy = BigDecimal.ZERO;
            BigDecimal totalSell = BigDecimal.ZERO;

            long SellQty = 0;
            long BuyQty = 0;

            BigDecimal GainOrLoss = BigDecimal.ZERO;
            BigDecimal GainOrLossPercent = BigDecimal.ZERO;

            for (Transactions transaction : transactions) {
                if ("buy".equals(transaction.getType())) {
                    totalBuy = totalBuy.add(transaction.getTransactPrice().multiply(BigDecimal.valueOf(transaction.getQuantity())));
                    BuyQty += transaction.getQuantity();
                }
                if ("sell".equals(transaction.getType())) {
                    totalSell = totalSell.add(transaction.getTransactPrice().multiply(BigDecimal.valueOf(transaction.getQuantity())));
                    SellQty += transaction.getQuantity();
                }
            }


            if (SellQty != 0 && BuyQty != 0) {
                BigDecimal avgSell = totalSell.divide(BigDecimal.valueOf(SellQty), 2, RoundingMode.HALF_UP);
                BigDecimal avgBuy = totalBuy.divide(BigDecimal.valueOf(BuyQty), 2, RoundingMode.HALF_UP);

                GainOrLoss = avgSell.subtract(avgBuy).multiply(BigDecimal.valueOf(SellQty));
                GainOrLossPercent = GainOrLoss.divide(totalBuy, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            PortfolioDTO portfolioDTO = PortfolioDTO.builder()
                    .totalBuyPrice(totalBuy)
                    .totalPnL(GainOrLoss)
                    .totalPnLpercent(GainOrLossPercent)
                    .holdingsResponseDTO(holdingsResponseDTO)
                    .build();

            return ResponseEntity.ok(portfolioDTO).getBody();

    }

    }

