package com.portfolioapp.stocks.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.portfolioapp.stocks.dto.HoldingsResponseDTO;
import com.portfolioapp.stocks.dto.PortfolioDTO;
import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.exception.TransactionNotFoundException;
import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.service.impl.PortfolioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserStocksService userStocksService;

    @Mock
    private StocksService stocksService;

    @Mock
    private TransactionService transactionService;

    @Spy
    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Test
    void testGetHoldings_success() {
        long userId = 1L;
        User user = new User(); // Create a user object
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        List<StockSummaryDTO> stockSummaries = new ArrayList<>();
        StockSummaryDTO stockSummary = new StockSummaryDTO();
        stockSummary.setStockId(1L);
        stockSummary.setTotalQuantity(10L);
        stockSummaries.add(stockSummary);
        when(userStocksService.getStockSummariesByUserId(userId)).thenReturn(stockSummaries);

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setStockName("Test Stock");
        stock.setClosePrice(BigDecimal.valueOf(100));
        when(stocksService.getReferenceById(1L)).thenReturn(stock);

        BigDecimal avgPrice = BigDecimal.valueOf(90);
        when(userStocksService.findAvgPrice(userId, 1L)).thenReturn(avgPrice);

        HoldingsResponseDTO holdingsResponse = portfolioService.getHoldings(userId);

        // Assert expected values in the response
        assertEquals(1, holdingsResponse.getHoldings().size());
        assertEquals("Test Stock", holdingsResponse.getHoldings().get(0).getStockName());
        assertEquals(BigDecimal.valueOf(1000), holdingsResponse.getTotalHoldings());
    }
    @Test
    void testGetHoldings_userNotFound() {
        long userId = 1L;
        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        try {
            portfolioService.getHoldings(userId);
            fail("Expected UserNotFoundException");
        } catch (UserNotFoundException e) {
            assertEquals("No data found for userId:" + userId, e.getMessage());
            verify(userService).findUserById(userId);
            verifyNoMoreInteractions(userStocksService, stocksService, userStocksService);
        }
    }

    @Test
    void testGetHoldings_noStockSummaries() {
        long userId = 1L;
        User user = new User(); // Create a user object
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));
        when(userStocksService.getStockSummariesByUserId(userId)).thenReturn(Collections.emptyList());

        try {
            portfolioService.getHoldings(userId);
            fail("Expected UserNotFoundException");
        } catch (UserNotFoundException e) {
            assertEquals("No data found for userId:" + userId, e.getMessage());
            verify(userService).findUserById(userId);
            verify(userStocksService).getStockSummariesByUserId(userId);
            verifyNoMoreInteractions(stocksService, userStocksService);
        }
    }

    @Test
    void testGetPortfolio_success() {
        long userId = 1L;
        User user = new User();
        when(userService.findUserById(userId)).thenReturn(Optional.of(user)).thenReturn(Optional.of(user));

        List<Transactions> transactions = new ArrayList<>();


        transactions.add(Transactions.builder()
                .transactPrice(BigDecimal.valueOf(90))
                .type("buy")
                .quantity(100L)
                .stockId(50001L)
                .userId(userId)
                .build());

        transactions.add(Transactions.builder()
                .transactPrice(BigDecimal.valueOf(110))
                .type("sell")
                .quantity(50L)
                .stockId(50001L)
                .userId(userId)
                .build());


        when(transactionService.findByUserId(userId)).thenReturn(transactions);

        HoldingsResponseDTO holdingsResponse = new HoldingsResponseDTO();
        doReturn(holdingsResponse).when(portfolioService).getHoldings(userId);

        PortfolioDTO portfolioDTO = portfolioService.getPortfolio(userId);

        assertEquals(BigDecimal.valueOf(9000), portfolioDTO.getTotalBuyPrice());
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2), portfolioDTO.getTotalPnL());
        assertEquals(11.11, portfolioDTO.getTotalPnLpercent().doubleValue(), 0.01);
        verify(transactionService).findByUserId(userId);
        verify(portfolioService).getHoldings(userId);
    }



    @Test
    void testGetPortfolio_noTransactions() {
        long userId = 1L;
        User user = new User(); // Create a user object
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));
        when(transactionService.findByUserId(userId)).thenReturn(Collections.emptyList());

        try {
            portfolioService.getPortfolio(userId);
        } catch (TransactionNotFoundException e) {
            assertEquals("No transaction found for user id: " + userId, e.getMessage());
            verify(userService).findUserById(userId);
            verify(transactionService).findByUserId(userId);
        }
    }


}
