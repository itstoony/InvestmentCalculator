package br.com.github.itstoony.investmentcalculator.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentFilterDTO {

    private LocalDateTime dateTime;

}
