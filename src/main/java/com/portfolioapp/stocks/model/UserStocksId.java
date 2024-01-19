package com.portfolioapp.stocks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStocksId implements Serializable {

    @Column(name = "userId")
    private Long userId;

    @Column(name = "purchasePrice")
    private BigDecimal purchasePrice;
    @Column(name = "stockId")
    private Long stockId;


}
