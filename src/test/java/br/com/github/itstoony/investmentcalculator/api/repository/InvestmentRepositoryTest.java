package br.com.github.itstoony.investmentcalculator.api.repository;

import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class InvestmentRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    InvestmentRepository repository;

    @Test
    @DisplayName("Should filter investments by date")
    public void findByDate() {
        // scenery
        LocalDateTime begin = LocalDateTime.of(2023, 1, 15, 15, 26);
        LocalDateTime end = LocalDateTime.of(2023, 2, 19, 15, 26);
        Investment investment = createNewInvestment(begin);

        entityManager.persist(investment);

        // execution
        Page<Investment> page = repository.findByDate(begin, end,  PageRequest.of(0, 10));

        // validation
        assertThat(page.getContent()).contains(investment);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getPageable().getPageSize()).isEqualTo(10);
        assertThat(page.getPageable().getPageNumber()).isEqualTo(0);

    }

    private static Investment createNewInvestment(LocalDateTime date) {
        return Investment.builder()
                .type(InvestmentType.CDB)
                .date(date)
                .investmentValue(new BigDecimal("200.00"))
                .build();
    }

}
