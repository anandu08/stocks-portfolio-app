package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.impl.UserStocksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserStocksServiceTest {

    @Mock
    private UserStocksRepo userStocksRepo;

    @InjectMocks
    private UserStocksServiceImpl userStocksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateQuantity_UserStockExists_QuantityUpdated() {
        UserStocksId userStocksId = new UserStocksId(1L, new BigDecimal("100.00"), 2L);
        UserStocks userStocks = new UserStocks(userStocksId, 5L);

        when(userStocksRepo.findById(userStocksId)).thenReturn(Optional.of(userStocks));

        userStocksService.updateQuantity(userStocksId, 10L);

        assertEquals(10L, userStocks.getQuantity());
        verify(userStocksRepo, times(1)).save(userStocks);
    }

    @Test
    void updateQuantity_UserStockNotFound_StockNotFoundExceptionThrown() {
        UserStocksId userStocksId = new UserStocksId(1L, new BigDecimal("100.00"), 2L);

        when(userStocksRepo.findById(userStocksId)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> userStocksService.updateQuantity(userStocksId, 10L));
        verify(userStocksRepo, never()).save(any());
    }


}
