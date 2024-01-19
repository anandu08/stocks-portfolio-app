package com.portfolioapp.stocks.repository;
import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserStocksRepoTest {

    @Mock
    private UserStocksRepo userStocksRepo;



    @Test
    public void testFindByUserIdAndStockId() {
        // Given
        long userId = 1L;
        long stockId = 123L;
        UserStocks userStocks = new UserStocks(new UserStocksId(userId, BigDecimal.TEN, stockId), 5L);

        // When
        when(userStocksRepo.findByUserIdAndStockId(userId, stockId)).thenReturn(Collections.singletonList(userStocks));

        // Then
        List<UserStocks> result = userStocksRepo.findByUserIdAndStockId(userId, stockId);
        assertEquals(1, result.size());
        assertEquals(userStocks, result.get(0));
        verify(userStocksRepo, times(1)).findByUserIdAndStockId(userId, stockId);
    }

    @Test
    public void testGetStockSummariesByUserId() {
        // Given
        Long userId = 1L;
        StockSummaryDTO stockSummaryDTO = new StockSummaryDTO(123L, 10L);

        // When
        when(userStocksRepo.getStockSummariesByUserId(userId)).thenReturn(Collections.singletonList(stockSummaryDTO));

        // Then
        List<StockSummaryDTO> result = userStocksRepo.getStockSummariesByUserId(userId);
        assertEquals(1, result.size());
        assertEquals(stockSummaryDTO, result.get(0));
        verify(userStocksRepo, times(1)).getStockSummariesByUserId(userId);
    }
}
