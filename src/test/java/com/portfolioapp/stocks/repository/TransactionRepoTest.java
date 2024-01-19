package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.Transactions;
import com.portfolioapp.stocks.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class TransactionRepoTest {

    @Mock
    private TransactionRepo transactionRepo;


    @Test
    public void testFindByUserId() {
        // Given
        Long userId = 1L;
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(new Transactions(1, userId, 123L, "BUY", BigDecimal.TEN, 5L));

        // When
        when(transactionRepo.findByUserId(userId)).thenReturn(transactionsList);

        // Then
        List<Transactions> result = transactionRepo.findByUserId(userId);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(transactionRepo, times(1)).findByUserId(userId);
    }

    @Test
    public void testSaveTransaction() {
        // Given
        Transactions transaction = new Transactions(1, 1L, 123L, "BUY", BigDecimal.TEN, 5L);

        // When
        when(transactionRepo.save(transaction)).thenReturn(transaction);

        // Then
        Transactions savedTransaction = transactionRepo.save(transaction);
        assertEquals(transaction, savedTransaction);
        verify(transactionRepo, times(1)).save(transaction);
    }

    @Test
    public void testGetTransactionById() {
        // Given
        Transactions transaction = new Transactions(1, 1L, 123L, "BUY", BigDecimal.TEN, 5L);

        // When
        when(transactionRepo.findById(1)).thenReturn(Optional.of(transaction));

        // Then
        Optional<Transactions> result = transactionRepo.findById(1);
        assertEquals(transaction, result.orElse(null));
        verify(transactionRepo, times(1)).findById(1);
    }
}
