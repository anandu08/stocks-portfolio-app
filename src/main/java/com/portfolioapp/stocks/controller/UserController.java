package com.portfolioapp.stocks.controller;


import com.portfolioapp.stocks.dto.UserRequestDTO;
import com.portfolioapp.stocks.exception.DataNotFoundException;
import com.portfolioapp.stocks.exception.InvalidQuantityException;
import com.portfolioapp.stocks.exception.StockNotAvailableException;
import com.portfolioapp.stocks.exception.StockNotFoundException;
import com.portfolioapp.stocks.model.Stock;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.service.StocksService;
import com.portfolioapp.stocks.service.TransactionService;
import com.portfolioapp.stocks.service.UserService;
import com.portfolioapp.stocks.service.UserStocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class UserController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserStocksService userStocksService;
    @Autowired
    private StocksService stocksService;
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User newUser = User.builder()
                    .name(userRequestDTO.getName())
                    .email(userRequestDTO.getEmail())
                    .build();
            userService.saveUser(newUser);

            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    @PostMapping(path = "/transact/{userId}")
    public String buyOrSell(@PathVariable long userId,
                            @RequestBody Map<String, Object> transactionDetails) {
        try {

            Optional<User> user = userService.findUserById(userId);
            if (user.isEmpty()) {
                throw new DataNotFoundException("User not found.");
            }


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

            if (stock == null) {
                throw new IllegalArgumentException("Stock not found.");
            }

            if (quantity <= 0) {
                throw new IllegalArgumentException("Invalid quantity. Quantity must be greater than 0.");
            }

            if ("buy".equalsIgnoreCase(type)) {
                transactionService.buyStock(userId, stock, quantity);
                return "Stock bought successfully.";
            } else if ("sell".equalsIgnoreCase(type)) {
                if (transactionService.sellStock(userId, stock, quantity)) {
                    return "Stock sold successfully.";
                } else {
                    throw new StockNotAvailableException("No such stock.");
                }
            } else {
                throw new IllegalArgumentException("Invalid transaction type.");
            }
        } catch (StockNotFoundException | InvalidQuantityException | StockNotAvailableException | IllegalArgumentException e) {
            return e.getMessage();
        }
    catch (Exception e) {
        return "An unexpected error occurred.";
    }

    }
}
