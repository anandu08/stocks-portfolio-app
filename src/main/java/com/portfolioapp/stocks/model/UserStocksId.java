package com.portfolioapp.stocks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class UserStocksId implements Serializable {

    private long userId;


    private BigDecimal purchasePrice;

    private String stockId;

}
