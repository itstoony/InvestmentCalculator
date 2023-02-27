package br.com.github.itstoony.investmentcalculator.api.controller;

import br.com.github.itstoony.investmentcalculator.api.model.dto.CalculatedValues;
import br.com.github.itstoony.investmentcalculator.api.model.dto.Simulation;
import br.com.github.itstoony.investmentcalculator.api.service.CalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CalculatorController.class)
public class CalculatorControllerTest {

    static String API = "/api/calculator";

    @Autowired
    MockMvc mvc;

    @MockBean
    CalculatorService service;

    @Test
    @DisplayName("Should return final value by investment value, years and percentage")
    public void calculateFinalValueTest() throws Exception {
        // scenery
        Simulation dto = createSimulation();
        CalculatedValues calculatedValue = createCalculatedValues();

        BDDMockito.given( service.simulateInvestment(dto) ).willReturn(calculatedValue);

        String json = new ObjectMapper().writeValueAsString(dto);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/calculate"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect( status().isOk() )
                .andExpect( jsonPath("fullValue").value(calculatedValue.getFullValue()) )
                .andExpect( jsonPath("valueWithoutFees").value(calculatedValue.getValueWithoutFees()) )
                .andExpect( jsonPath("totalOfFees").value(calculatedValue.getTotalOfFees() ) );

    }

    public static Simulation createSimulation() {
        return Simulation.builder()
                .monthlyValue(new BigDecimal("200"))
                .percentage(new BigDecimal("0.1265"))
                .years(5)
                .build();
    }

    public static CalculatedValues createCalculatedValues() {
        return CalculatedValues.builder()
                .fullValue(new BigDecimal("16796.67467316218"))
                .valueWithoutFees(new BigDecimal("12000"))
                .totalOfFees(new BigDecimal("4796.674673162179"))
                .build();
    }
}
