package com.portfolioapp.stocks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "users_stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStocks {

    @EmbeddedId
    private UserStocksId id;

    @Column(name = "quantity")
    private Long quantity;


}