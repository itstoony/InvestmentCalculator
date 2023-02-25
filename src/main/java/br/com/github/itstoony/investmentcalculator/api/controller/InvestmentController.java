package br.com.github.itstoony.investmentcalculator.api.controller;

import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    private final ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentDTO newInvestment(@RequestBody InvestmentDTO dto) {

        Investment investment = modelMapper.map(dto, Investment.class);

        Investment savedInvestment = investmentService.save(investment);

        return modelMapper.map(savedInvestment, InvestmentDTO.class);

    }
}
