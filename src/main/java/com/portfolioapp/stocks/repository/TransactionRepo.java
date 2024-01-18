package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions,Integer> {

    List<Transactions> findByUserId(Long userId);
}
