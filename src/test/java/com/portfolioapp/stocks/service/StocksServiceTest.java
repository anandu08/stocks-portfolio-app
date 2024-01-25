package com.portfolioapp.stocks.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.exception.StockUpdateException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.StocksService;
import com.portfolioapp.stocks.service.impl.StocksServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StocksServiceTest {

    @Mock
    private StocksRepo stocksRepo;


    @InjectMocks
    private StocksServiceImpl stocksService;

    @Test
    void testUpdater_IOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Test exception"));

        assertThrows(StockUpdateException.class, () -> stocksService.updater(file));
    }



    @Test
    void testGetReferenceById_success() {
        Long stockId = 1L;
        Stock stock = new Stock();
        when(stocksRepo.getReferenceById(stockId)).thenReturn(stock);

        Stock foundStock = stocksService.getReferenceById(stockId);

        assertSame(stock, foundStock);
    }

    @Test
    void testGetReferenceById_notFound() {
        Long stockId = 2L;
        when(stocksRepo.getReferenceById(stockId)).thenReturn(null);

        assertThrows(StockNotAvailableException.class, () -> stocksService.getReferenceById(stockId));
    }

    @Test
    void testFindStockById_success() {
        Long stockId = 3L;
        Stock stock = new Stock();
        when(stocksRepo.findStockById(stockId)).thenReturn(stock);

        Stock foundStock = stocksService.findStockById(stockId);

        assertSame(stock, foundStock);
    }

    @Test
    void testFindStockById_notFound() {
        Long stockId = 4L;
        when(stocksRepo.findStockById(stockId)).thenReturn(null);

        assertThrows(StockNotAvailableException.class, () -> stocksService.findStockById(stockId));
    }

}

