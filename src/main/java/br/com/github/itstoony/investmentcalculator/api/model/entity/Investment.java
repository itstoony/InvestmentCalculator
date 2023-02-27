package br.com.github.itstoony.investmentcalculator.api.model.entity;

import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import jakarta.persistence.*;
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
@Entity
@Table(name = "investment")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "investmentValue")
    private BigDecimal investmentValue;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    @Column(name = "date")
    private LocalDateTime date;

}
