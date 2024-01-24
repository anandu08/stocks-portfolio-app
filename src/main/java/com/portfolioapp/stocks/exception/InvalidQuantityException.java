package com.portfolioapp.stocks.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(long qty) {
        super("Invalid Quantity: " + qty + "\nIt should be greater than or equal to 0");
    }
}