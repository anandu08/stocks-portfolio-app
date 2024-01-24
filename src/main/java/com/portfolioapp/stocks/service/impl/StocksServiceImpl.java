package com.portfolioapp.stocks.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.exception.StockUpdateException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StocksServiceImpl implements StocksService {

    private final StocksRepo stocksRepo;


    @Override
    public void updater(MultipartFile file) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = csvReader.readNext();

            if (header != null) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Stock stock = mapToStock(line);
                    stocksRepo.save(stock);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new StockUpdateException("Error updating stocks from CSV: " + e.getMessage());
        }
    }

    @Override
    public Stock getReferenceById(Long stockId) {
        Stock stock =  stocksRepo.getReferenceById(stockId);
        if(stock == null)
            throw new StockNotAvailableException(stockId);
        return stock;
    }

    @Override
    public Stock findStockById(Long id) {
        Stock stock =  stocksRepo.findStockById(id);
        if(stock == null)
            throw  new StockNotAvailableException(id);
        return stock;
    }

    private Stock mapToStock(String[] line) {
        Stock stock = new Stock();
        stock.setId(Long.valueOf(line[0]));
        stock.setStockName(line[1]);
        stock.setOpenPrice(new BigDecimal(line[4]));
        stock.setClosePrice(new BigDecimal(line[7]));
        stock.setHigh(new BigDecimal(line[5]));
        stock.setLow(new BigDecimal(line[6]));
        System.out.println(stock);

        return stock;
    }
}
