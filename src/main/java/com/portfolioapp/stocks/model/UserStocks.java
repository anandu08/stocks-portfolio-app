package com.portfolioapp.stocks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users-stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserStocksId.class)
public class UserStocks {

    @Id
    private long userId;

@Id
    private BigDecimal purchasePrice;
@Id
    private String stockId;


    private long quantity;

    @OneToMany(mappedBy = "userStocks", cascade = CascadeType.ALL)
    private Set<Stock> stocks = new HashSet<>();


}