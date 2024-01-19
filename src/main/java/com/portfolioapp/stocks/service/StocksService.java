package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.Stock;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StocksService {

    public void updater(MultipartFile file);

    Stock getReferenceById(Long stockId);
    Stock findStockById(Long id);



}
