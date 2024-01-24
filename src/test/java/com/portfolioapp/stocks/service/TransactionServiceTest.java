package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.repository.TransactionRepo;
import com.portfolioapp.stocks.repository.UserStocksRepo;
import com.portfolioapp.stocks.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private StocksRepo stocksRepo;

    @Mock
    private UserStocksRepo userStocksRepo;

    @Mock
    private TransactionRepo transactionRepo;


    @Mock
    private UserStocksService userStocksService;

    @Test
    void buyStock_ValidStockAndQuantity_TransactionSuccessful() {
        long userId = 1L;
        Stock stock = new Stock();
        long quantity = 10L;

        when(userStocksRepo.findById(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> transactionService.buyStock(userId, stock, quantity));

        Mockito.verify(transactionRepo, Mockito.times(1)).save(any());
        Mockito.verify(userStocksRepo, Mockito.times(1)).save(any());
    }

    @Test
    void sellStock_ValidStockAndQuantity_TransactionSuccessful() {
        long userId = 1L;
        Stock stock = new Stock();
        long quantity = 5L;

        UserStocks userStocks = new UserStocks();
        UserStocksId userStocksId = new UserStocksId();
        userStocksId.setUserId(userId);
        userStocksId.setStockId(stock.getId());
        userStocksId.setPurchasePrice(new BigDecimal("100.00"));
        userStocks.setId(userStocksId);
        userStocks.setQuantity(quantity);

        List<UserStocks> userStocksList = List.of(userStocks);

        when(userStocksRepo.findByUserIdAndStockId(anyLong(), any())).thenReturn(userStocksList);


           }


}
