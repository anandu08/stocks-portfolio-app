package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.exception.StockUpdateException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.service.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StocksController {
    private final StocksService stocksService;

    @PostMapping("/update-stocks")
    public ResponseEntity<String> update(@RequestParam("file") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                stocksService.updater(file);
                return ResponseEntity.ok("Stocks updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("No file provided.");
            }
        } catch (StockUpdateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating stocks: " + e.getMessage());
        }
    }

    @GetMapping("get-stock/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {

            Stock stock = stocksService.findStockById(id);
                return ResponseEntity.ok(stock);

    }
}
