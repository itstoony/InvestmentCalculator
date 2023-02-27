package br.com.github.itstoony.investmentcalculator.api.controller;

import br.com.github.itstoony.investmentcalculator.api.model.dto.CalculatedValues;
import br.com.github.itstoony.investmentcalculator.api.model.dto.Simulation;
import br.com.github.itstoony.investmentcalculator.api.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    @PostMapping("/calculate")
    public CalculatedValues calculateFinalValue(@RequestBody Simulation dto) {
        return calculatorService.simulateInvestment(dto);
    }

}
