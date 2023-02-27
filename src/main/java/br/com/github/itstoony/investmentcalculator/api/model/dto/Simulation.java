package br.com.github.itstoony.investmentcalculator.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Simulation {

    private BigDecimal monthlyValue;

    private BigDecimal percentage;

    private Integer years;

}
