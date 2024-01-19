package com.portfolioapp.stocks.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @Column(name = "stocksId", unique = true, nullable = false)
    private Long id;


    @Column(name = "stockName", columnDefinition = "VARCHAR(255) DEFAULT 'UNDEFINED'")
    private String stockName;

    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal high;
    private BigDecimal low;



}
