package com.portfolioapp.stocks.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

    @Id
    private int TrId;
    private long userId;

    private String stockId;
    private String type;
    private BigDecimal transactPrice;
    private long quantity;


}
