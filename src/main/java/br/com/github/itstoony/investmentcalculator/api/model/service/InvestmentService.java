package br.com.github.itstoony.investmentcalculator.api.model.service;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentFilterDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService {

    public Investment save(Investment investment) {
        return null;
    }

    public Page<Investment> find(InvestmentFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    public Investment findById(Long id) {
        return null;
    }

    public Investment update(Investment investment, InvestmentDTO update) {
        return null;
    }

    public void delete(Investment investment) {

    }
}
