package com.portfolioapp.stocks.repository;

import com.portfolioapp.stocks.utils.TransactionsLogic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionsLogic,Integer> {
}
