package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.repository.StocksRepo;
import com.portfolioapp.stocks.service.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StocksController {
    @Autowired
    private StocksService stocksService;
    @Autowired
    private StocksRepo stocksRepo;



    @PostMapping("/update-stocks")
    public void update(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            // Process the CSV file using stocksService.updater(file)
            stocksService.updater(file);
        } else {
            // Handle the case where no file is provided
            System.out.println("No file provided");
        }
    }

    @RequestMapping("get-stock/{id}")
    public Stock getStock(@PathVariable String id){
        return stocksRepo.findStockById(id);


    }
}
