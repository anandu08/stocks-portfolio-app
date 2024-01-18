package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StocksController {
    @Autowired
    private StocksService stocksService;
    @Autowired
    private StocksRepo stocksRepo;



    @PostMapping("/update-stocks")
    public void update() {
        String filePath = "/Users/anandus/Downloads/cm12JAN2024bhav.csv";
        System.out.println("I am here");
        stocksService.updater(filePath);
    }
    @RequestMapping("get-stock/{id}")
    public Stock getStock(@PathVariable String id){
        return stocksRepo.findStockById(id);


    }
}
