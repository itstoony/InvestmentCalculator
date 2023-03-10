package br.com.github.itstoony.investmentcalculator.api.controller;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentFilterDTO;
import br.com.github.itstoony.investmentcalculator.api.model.dto.UpdatingInvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.enums.InvestmentType;
import br.com.github.itstoony.investmentcalculator.api.service.InvestmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InvestmentController.class)
public class InvestmentControllerTest {

    static String API = "/investment/api";

    @Autowired
    MockMvc mvc;

    @MockBean
    InvestmentService service;

    @Test
    @DisplayName("Should create an investment")
    public void createInvestmentTest() throws Exception {
        // scenery
        InvestmentDTO dto = createNewInvestmentDTO();

        Investment investment = createNewInvestment();
        investment.setId(1L);

        BDDMockito.given( service.save(Mockito.any(Investment.class)) ).willReturn( investment );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expectedDate = formatter.format(dto.getDate().truncatedTo(ChronoUnit.SECONDS));

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(dto);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("value").value(dto.getValue().setScale(1, RoundingMode.HALF_UP)))
                .andExpect(jsonPath("type").value(InvestmentType.CDB.toString()))
                .andExpect(jsonPath("date").value(expectedDate));

    }

    @Test
    @DisplayName("Should throw a validation error when trying to create an investment without required fields")
    public void createInvalidInvestmentTest() throws Exception {
        // scenery
        String json = new ObjectMapper().writeValueAsString(new InvestmentDTO());

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(3)) ); // has "3" because 3 fields are validated in InvestmentDTO
    }

    @Test
    @DisplayName("Should return a list with all investments")
    public void getAllInvestmentsTest() throws Exception {
        // scenery
        Investment investment = createNewInvestment();
        investment.setId(1L);

        BDDMockito.given( service.find(Mockito.any(InvestmentFilterDTO.class), Mockito.any(Pageable.class) ))
                .willReturn(new PageImpl<>(Collections.singletonList(investment), Pageable.ofSize( 20), 1));

        String queryString = String.format("?dateTime=%s", investment.getDate());

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect( status().isOk() )
                .andExpect( jsonPath("content", hasSize(1)) )
                .andExpect( jsonPath("totalElements").value(1) )
                .andExpect( jsonPath("pageable.pageSize").value(20) )
                .andExpect( jsonPath("pageable.pageNumber").value(0) );

    }

    @Test
    @DisplayName("Should update an investment")
    public void updateInvestmentTest() throws Exception {
        // scenery
        Investment updatingInvestment = createNewInvestment();
        updatingInvestment.setId(1L);

        UpdatingInvestmentDTO update = UpdatingInvestmentDTO.builder()
                .date(LocalDateTime.of(2023, 2, 22, 17, 33))
                .value(new BigDecimal("500.00"))
                .type(InvestmentType.LCA)
                .build();

        Investment updated = Investment.builder()
                .id(1L)
                .date(update.getDate())
                .type(update.getType())
                .investmentValue(update.getValue())
                .build();

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(update);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expectedDate = formatter.format(update.getDate().truncatedTo(ChronoUnit.SECONDS));

        BDDMockito.given( service.findById(1L) ).willReturn(Optional.of(updatingInvestment));
        BDDMockito.given( service.update( Mockito.any(Investment.class), Mockito.any(InvestmentDTO.class) ) ).willReturn(updated);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/" + 1L))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect( jsonPath("id").value(1L) )
                .andExpect( jsonPath("date").value(expectedDate) )
                .andExpect( jsonPath("value").value(update.getValue().setScale(1, RoundingMode.HALF_UP)))
                .andExpect( jsonPath("type").value(update.getType().toString()));

    }

    @Test
    @DisplayName("Should return 'not found' when trying to update an invalid investment")
    public void updateInvalidIdInvestmentTest() throws Exception {
        // scenery
        Long id = 1L;

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(new InvestmentDTO());

        BDDMockito.given( service.findById(id) ).willReturn(Optional.empty());

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/" + id))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Should delete an investment")
    public void deleteInvestmentTest() throws Exception {
        // scenery
        Long id = 1L;

        Investment investment = createNewInvestment();
        investment.setId(id);

        BDDMockito.given( service.findById(id) ).willReturn(Optional.of(investment));

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/" + id));

        // validation
        mvc
                .perform(request)
                .andExpect( status().isNoContent() );
    }

    @Test
    @DisplayName("Should return 'not found' when trying to delete an invalid investment")
    public void deleteInvalidInvestmentTest() throws Exception {
        // scenery
        Long id = 1L;

        BDDMockito.given( service.findById(id) ).willReturn(Optional.empty());

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/" + id));

        // validation
        mvc
                .perform(request)
                .andExpect( status().isNotFound() );

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


