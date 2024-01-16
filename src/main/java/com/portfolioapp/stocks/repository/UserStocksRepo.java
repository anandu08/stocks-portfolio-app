package com.portfolioapp.stocks.repository;

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

    @Query("SELECT u.quantity, u.id.purchasePrice FROM UserStocks u JOIN u.stocks s WHERE u.id.userId = :userId AND s.id = :stockId")
    List<Object[]> findFieldsByUserIdAndStockId(@Param("userId") long userId, @Param("stockId") String stockId);




}
