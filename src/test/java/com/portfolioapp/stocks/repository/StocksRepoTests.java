package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StocksRepoTests {

    @Mock
    private StocksRepo stocksRepo;

    @Test
    public void testFindStockById() {
        Long stockId = 500009L;
        Stock mockStock = new Stock(stockId, "A.SARABHAI  ", BigDecimal.valueOf(44.00), BigDecimal.valueOf(44.00), BigDecimal.valueOf(44.00), BigDecimal.valueOf(44.00));
        when(stocksRepo.findStockById(stockId)).thenReturn(mockStock);

        Stock result = stocksRepo.findStockById(stockId);

        verify(stocksRepo, times(1)).findStockById(stockId);

        assertNotNull(result);
        assertEquals(stockId, result.getId());
        assertEquals("A.SARABHAI  ", result.getStockName());
    }
}
