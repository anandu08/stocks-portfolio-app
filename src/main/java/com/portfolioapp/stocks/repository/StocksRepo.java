package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepo extends JpaRepository<Stock, Long> {


    Stock findStockById(Long id);

}
