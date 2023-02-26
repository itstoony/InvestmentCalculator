package br.com.github.itstoony.investmentcalculator.api.controller;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentFilterDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    private final ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentDTO newInvestment( @RequestBody InvestmentDTO dto ) {

        Investment investment = modelMapper.map(dto, Investment.class);

        Investment savedInvestment = investmentService.save(investment);

        return modelMapper.map(savedInvestment, InvestmentDTO.class);

    }

    @GetMapping
    public Page<InvestmentDTO> getAllInvestment(InvestmentFilterDTO dto, Pageable pageRequest) {

        Page<Investment> result = investmentService.find(dto, pageRequest);

        List<InvestmentDTO> investmentDTOS = result
                .getContent()
                .stream()
                .map( entity -> modelMapper.map(entity, InvestmentDTO.class) )
                .toList();

        return new PageImpl<>(investmentDTOS, pageRequest, result.getTotalElements());

    }

    @PutMapping("/{id}")
    public InvestmentDTO update( @PathVariable Long id, @RequestBody InvestmentDTO dto) {
        Investment updating = investmentService.findById(id);

        Investment updated = investmentService.update(updating, dto);

        return modelMapper.map(updated, InvestmentDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Investment investment = investmentService.findById(id);

        investmentService.delete(investment);
    }
}
