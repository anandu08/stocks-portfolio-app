package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transactions,Integer> {
}
