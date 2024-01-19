package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.dto.StockSummaryDTO;
import com.portfolioapp.stocks.model.UserStocks;
import com.portfolioapp.stocks.model.UserStocksId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserStocksRepo extends JpaRepository<UserStocks, UserStocksId> {

    @Query("SELECT us FROM UserStocks us WHERE us.id.userId = :userId AND us.id.stockId = :stockId")
    List<UserStocks> findByUserIdAndStockId(@Param("userId") long userId, @Param("stockId") Long stockId);

    @Query("SELECT NEW com.portfolioapp.stocks.dto.StockSummaryDTO(us.id.stockId, SUM(us.quantity)) " +
            "FROM UserStocks us " +
            "WHERE us.id.userId = :userId " +
            "GROUP BY us.id.stockId")
    List<StockSummaryDTO> getStockSummariesByUserId(@Param("userId") Long userId);



}
