package com.portfolioapp.stocks.controller;

import com.portfolioapp.stocks.dto.UserRequestDTO;
import com.portfolioapp.stocks.exception.InvalidQuantityException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.service.StocksService;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class UserController {


    private final TransactionService transactionService;
    private final StocksService stocksService;
    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User existingUser = userService.findUserByEmail(userRequestDTO.getEmail());
            if (existingUser != null) {
                return ResponseEntity.status(400).body("User with this email already exists.");
            }
            User newUser = User.builder()
                    .name(userRequestDTO.getName())
                    .email(userRequestDTO.getEmail())
                    .build();
            userService.saveUser(newUser);

            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    @PostMapping(path = "/transact/{userId}")
    public String buyOrSell(@PathVariable long userId,
                            @RequestBody Map<String, Object> transactionDetails) {
        try {

            Optional<User> user = userService.findUserById(userId);



            if (!transactionDetails.containsKey("type") ||
                    !transactionDetails.containsKey("stockId") ||
                    !transactionDetails.containsKey("quantity")) {
                throw new IllegalArgumentException("Missing required fields. Please provide 'type', 'stockId', and 'quantity'.");
            }

            String type = (String) transactionDetails.get("type");
            long stockId;
            long quantity;

            if (!"buy".equalsIgnoreCase(type) && !"sell".equalsIgnoreCase(type)) {
                throw new IllegalArgumentException("Invalid transaction type. Use 'buy' or 'sell'.");
            }

            if (transactionDetails.get("stockId") == null) {
                throw new IllegalArgumentException("'stockId' is required and must be a valid number.");
            } else {
                try {
                    stockId = ((Number) transactionDetails.get("stockId")).longValue();
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("'stockId' must be a valid number.");
                }
            }

            if (transactionDetails.get("quantity") == null) {
                throw new IllegalArgumentException("'quantity' is required and must be a valid number.");
            } else {
                try {
                    quantity = ((Number) transactionDetails.get("quantity")).longValue();
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("'quantity' must be a valid number.");
                }
            }

            Stock stock = stocksService.findStockById(stockId);
            if ("buy".equalsIgnoreCase(type)) {
                transactionService.buyStock(userId, stock, quantity);
                return "Stock bought successfully.";
            } else if ("sell".equalsIgnoreCase(type)) {
             transactionService.sellStock(userId, stock, quantity);
                    return "Stock sold successfully.";

            } else {
                throw new IllegalArgumentException("Invalid transaction type.");
            }
        } catch (InvalidQuantityException | StockNotAvailableException | IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    catch (Exception e) {
            e.printStackTrace();
        return "An unexpected error occurred.";
    }

    }
}
