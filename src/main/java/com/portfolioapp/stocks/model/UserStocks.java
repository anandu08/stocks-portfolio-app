package com.portfolioapp.stocks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStocks {

    @EmbeddedId
    private UserStocksId id;

    @Column(name = "quantity")
    private long quantity;

    @OneToMany(mappedBy = "userStocks", cascade = CascadeType.ALL)
    private List<Stock> stocks;


}