package br.com.github.itstoony.investmentcalculator.api.service;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import br.com.github.itstoony.investmentcalculator.api.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class InvestmentServiceTest {

    InvestmentService service;

    @MockBean
    InvestmentRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new InvestmentService(repository);
    }

    @Test
    @DisplayName("Should save an investment")
    public void saveInvestmentTest() {
        // scenery
        Investment investment = createNewInvestment();
        investment.setId(1L);

        BDDMockito.given( repository.save(investment) ).willReturn(investment);

        // execution
        Investment savedInvestment = service.save(investment);

        // validation
        assertThat(savedInvestment.getId()).isNotNull();
        assertThat(savedInvestment.getDate()).isEqualTo(investment.getDate());
        assertThat(savedInvestment.getValue()).isEqualTo(investment.getValue());
        assertThat(savedInvestment.getType()).isEqualTo(investment.getType());

    }

    @Test
    @DisplayName("Should find an investment by it's id")
    public void findByIdTest() {
        // scenery
        Long id = 1L;

        Investment investment = createNewInvestment();
        investment.setId(id);

        BDDMockito.given( repository.findById(id) ).willReturn( Optional.of(investment) );

        // execution
        Optional<Investment> foundInvestment = service.findById(id);

        // validation
        assertThat(foundInvestment).isPresent();
        assertThat(foundInvestment.get().getId()).isEqualTo(id);
        assertThat(foundInvestment.get().getType()).isEqualTo(investment.getType());
        assertThat(foundInvestment.get().getDate()).isEqualTo(investment.getDate());
        assertThat(foundInvestment.get().getValue()).isEqualTo(investment.getValue());

    }

    @Test
    @DisplayName("Should return empty when trying to find an invalid investment")
    public void findInvalidInvestmentByIdTest() {
        // scenery
        Long id = 1L;

        BDDMockito.given( repository.findById(id) ).willReturn(Optional.empty());

        // execution
        Optional<Investment> foundInvestment = service.findById(id);

        // validation
        assertThat(foundInvestment).isEmpty();

    }

    private static InvestmentDTO createNewInvestmentDTO() {
        return InvestmentDTO.builder()
                .date(LocalDateTime.of(2023, 1, 15, 15, 26))
                .value(new BigDecimal("200.00"))
                .type(InvestmentType.CDB)
                .build();
    }

    private static Investment createNewInvestment() {
        return Investment.builder()
                .type(InvestmentType.CDB)
                .date(LocalDateTime.of(2023, 1, 15, 15, 26))
                .value(new BigDecimal("200.00"))
                .build();
    }

}
