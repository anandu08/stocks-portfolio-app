package com.portfolioapp.stocks.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StocksService {

    public void updater(MultipartFile file);



    }
