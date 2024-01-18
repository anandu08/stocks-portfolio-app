package com.portfolioapp.stocks.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

public class AvgPrice {

@EmbeddedId
private AvgPriceId avgPriceId;

    private Long quantity;
    private BigDecimal avgPrice;


}
