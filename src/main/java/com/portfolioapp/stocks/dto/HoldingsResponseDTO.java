package com.portfolioapp.stocks.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HoldingsResponseDTO {

    private List<HoldingsDTO> holdings;
    private BigDecimal totalHoldings;

}
