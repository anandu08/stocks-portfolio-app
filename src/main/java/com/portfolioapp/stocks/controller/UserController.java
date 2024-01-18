package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.dto.HoldingsDTO;
import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private StocksRepo stocksRepo;

    @Autowired
    private  UserStocksRepo userStocksRepo;

    @Autowired
    private TransactionService transactionService;


    @PostMapping(path = "/transact/{userId}")
    public String buyOrSell(@PathVariable long userId,
                            @RequestBody Map<String, Object> transactionDetails) {

        String type = (String) transactionDetails.get("type");
        String stockId = (String) transactionDetails.get("stockId");
        long quantity = Long.parseLong((String) transactionDetails.get("quantity"));

        Stock stock = stocksRepo.findStockById(stockId);

        if (stock == null) {
            return "Stock not found.";
        }

        if (quantity <= 0) {
            return "Invalid quantity. Quantity must be greater than 0.";
        }

        if ("buy".equalsIgnoreCase(type)) {

            transactionService.buyStock(userId, stock, quantity);

            return "Stock bought successfully.";
        }

        else if ("sell".equalsIgnoreCase(type)) {

            transactionService.sellStock(userId, stock, quantity);

            return "Stock sold successfully.";
        }

        else {

            return "Invalid transaction type.";
        }
    }
    @GetMapping(path = "/holdings/{userId}")
    public HoldingsResponseDTO getHoldings(@PathVariable long userId){


        List<StockSummaryDTO> rows = userStocksRepo.getStockSummariesByUserId(userId);
        HoldingsResponseDTO holdingsResponseDTO = new HoldingsResponseDTO();
        List<HoldingsDTO> stockHoldingsList = new ArrayList<>();

        BigDecimal totalHoldings = BigDecimal.ZERO;
        for(StockSummaryDTO row:rows)
        {
            String stockId = row.getStockId();
            Stock stock = stocksRepo.getReferenceById(stockId);
            BigDecimal stockHoldings = stock.getClosePrice().multiply(BigDecimal.valueOf(row.getTotalQuantity()));

            HoldingsDTO holdingsDTO = HoldingsDTO.builder()
                                                 .stockName(stock.getStockName())
                                                 .quantity(row.getTotalQuantity())
                                                 .userId(userId)
                                                 .stockId(stockId)
                                                 .StockHoldings(stockHoldings)
                                                 .build();

            totalHoldings = totalHoldings.add(stockHoldings);
            stockHoldingsList.add(holdingsDTO);



        }
        holdingsResponseDTO.setHoldings(stockHoldingsList);
        holdingsResponseDTO.setTotalHoldings(totalHoldings);





        return holdingsResponseDTO;
    }




}