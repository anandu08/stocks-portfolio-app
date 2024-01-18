package com.portfolioapp.stocks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UserStocksId implements Serializable {

    @Column(name = "userId")
    private Long userId;

    @Column(name = "purchasePrice")
    private BigDecimal purchasePrice;
    @Column(name = "stockId")
    private String stockId;

}
