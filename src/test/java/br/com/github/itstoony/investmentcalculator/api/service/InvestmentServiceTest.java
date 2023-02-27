package br.com.github.itstoony.investmentcalculator.api.service;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentFilterDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import br.com.github.itstoony.investmentcalculator.api.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

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
        assertThat(savedInvestment.getInvestmentValue()).isEqualTo(investment.getInvestmentValue());
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
        assertThat(foundInvestment.get().getInvestmentValue()).isEqualTo(investment.getInvestmentValue());

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

    @Test
    @DisplayName("Should delete an investment")
    public void deleteInvestmentTest() {
        // scenery
        Investment investment = Investment.builder().id(1L).build();

        // execution
        assertDoesNotThrow( () -> service.delete(investment) );

        // validation
        verify(repository, times(1)).delete(investment);

    }

    @Test
    @DisplayName("Should throw an exception when trying to delete an unsaved investment")
    public void deleteInvalidInvestmentTest() {
        // scenery
        Investment investment = createNewInvestment();
        String error = "Can't delete an unsaved investment";

        // execution
        Throwable ex = catchThrowable( () -> service.delete(investment) );

        // validation
        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
        assertThat(ex).hasMessage(error);

        verify(repository, never()).delete(investment);

    }

    @Test
    @DisplayName("Should update an investment")
    public void updateInvestmentTest() {
        // scenery
        Long id = 1L;

        Investment updatingInvestment = createNewInvestment();
        updatingInvestment.setId(id);

        InvestmentDTO update = createNewInvestmentDTO();

        Investment updatedInvestment = Investment.builder()
                .id(id)
                .investmentValue(update.getValue())
                .type(update.getType())
                .date(update.getDate())
                .build();

        BDDMockito.given( repository.save(any()) ).willReturn(updatedInvestment);

        // execution
        Investment returnedInvestment = service.update(updatingInvestment, update);

        // validation
        assertThat(returnedInvestment.getId()).isEqualTo(id);
        assertThat(returnedInvestment.getInvestmentValue()).isEqualTo(update.getValue());
        assertThat(returnedInvestment.getType()).isEqualTo(update.getType());
        assertThat(returnedInvestment.getDate()).isEqualTo(update.getDate());

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw an exception when trying to update an unsaved investment")
    public void updateUnsavedInvestment() {
        // scenery
        Investment investment = createNewInvestment();
        InvestmentDTO update = createNewInvestmentDTO();
        String error = "Can't update an unsaved investment";

        // execution
        Throwable ex = catchThrowable(() -> service.update(investment, update));

        // validation
        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
        assertThat(ex).hasMessage(error);

        verify(repository, never()).save(any());

    }

    @Test
    @DisplayName("Should filter investments by date")
    public void findTest() {
        // scenery
        InvestmentFilterDTO filterDTO = InvestmentFilterDTO.builder()
                .begin(LocalDateTime.of(2023, 1, 15, 15, 26))
                .build();

        Investment investment = createNewInvestment();
        investment.setId(1L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Investment> list = Collections.singletonList(investment);

        PageImpl<Investment> page = new PageImpl<>(list, pageRequest, 1);

        when( repository.findByDate(filterDTO.getBegin(), filterDTO.getEnd(), pageRequest) ).thenReturn(page);

        // execution
        Page<Investment> result = service.find(filterDTO, pageRequest);

        // validation
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

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
                .investmentValue(new BigDecimal("200.00"))
                .build();
    }

}
