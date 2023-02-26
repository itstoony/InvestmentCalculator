package br.com.github.itstoony.investmentcalculator.api.model.dto;

import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import jakarta.validation.constraints.NotNull;
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
public class InvestmentDTO {

    private Long id;

    @NotNull
    private BigDecimal value;

    @NotNull
    private InvestmentType type;

    @NotNull
    private LocalDateTime date;

}
