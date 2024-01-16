package com.portfolioapp.stocks.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Component
public class UpdateStocks {

    private final StocksRepo repository;

    @Autowired
    public UpdateStocks(StocksRepo repository) {
        this.repository = repository;
    }

    public void updater(String filePath) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] header = csvReader.readNext();

            if (header != null) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Stock stock = mapToStock(line);
                    repository.save(stock);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private Stock mapToStock(String[] line) {
        Stock stock = new Stock();
        stock.setId(line[0]);
        stock.setOpenPrice(new BigDecimal(line[2]));
        stock.setClosePrice(new BigDecimal(line[5]));
        stock.setHigh(new BigDecimal(line[3]));
        stock.setLow(new BigDecimal(line[4]));
        // Add other fields as needed
        System.out.println(stock);

        return stock;
    }
}
