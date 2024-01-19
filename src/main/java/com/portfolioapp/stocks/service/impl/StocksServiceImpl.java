package com.portfolioapp.stocks.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.portfolioapp.stocks.exception.StockUpdateException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
public class StocksServiceImpl implements StocksService {

    private StocksRepo repository;

    @Autowired
    public void setRepository(StocksRepo repository) {
        this.repository = repository;
    }


    @Override
    public void updater(MultipartFile file) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = csvReader.readNext();

            if (header != null) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Stock stock = mapToStock(line);
                    repository.save(stock);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new StockUpdateException("Error updating stocks from CSV: " + e.getMessage());
        }
    }

    @Override
    public Stock getReferenceById(Long stockId) {
        return repository.getReferenceById(stockId);
    }

    @Override
    public Stock findStockById(Long id) {
        return repository.findStockById(id);
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
