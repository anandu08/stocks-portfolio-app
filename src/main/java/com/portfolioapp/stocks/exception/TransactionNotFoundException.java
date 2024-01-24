package com.portfolioapp.stocks.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(long userId) {
        super("No transaction found for user id: "+ userId);
    }
}