package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.AvgPrice;
import com.portfolioapp.stocks.model.AvgPriceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvgPriceRepo extends JpaRepository<AvgPrice, AvgPriceId> {

}
