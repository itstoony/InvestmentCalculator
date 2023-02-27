package br.com.github.itstoony.investmentcalculator.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CalculatedValues {

    private BigDecimal fullValue;

    private BigDecimal valueWithoutFees;

    private BigDecimal totalOfFees;

}
