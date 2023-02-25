package br.com.github.itstoony.investmentcalculator.api.model.entity;

import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Investment {

    private Long id;

    private BigDecimal value;

    private InvestmentType type;

    private LocalDateTime date;

}
