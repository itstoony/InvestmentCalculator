package br.com.github.itstoony.investmentcalculator.api.service;

import br.com.github.itstoony.investmentcalculator.api.repository.InvestmentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class InvestmentServiceTest {

    InvestmentService service;

    @MockBean
    InvestmentRepository repository;

}
