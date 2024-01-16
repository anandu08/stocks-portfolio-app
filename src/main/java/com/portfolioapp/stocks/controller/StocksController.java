package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.utils.UpdateStocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StocksController {

    private final UpdateStocks updateStocks;
    private final StocksRepo stocksRepo;

    @Autowired
    public StocksController(UpdateStocks updateStocks, StocksRepo stocksRepo) {
        this.updateStocks = updateStocks;
        this.stocksRepo = stocksRepo;
    }

    @RequestMapping("/update-stocks")
    public void update() {
        String filePath = "/Users/anandus/Downloads/cm12JAN2024bhav.csv";
        System.out.println("I am here");
        updateStocks.updater(filePath);
    }
    @RequestMapping("get-stock/{id}")
    public Stock getStock(@PathVariable String id){
        Stock stock = stocksRepo.findStockById(id);
        return stock;


    }
}
