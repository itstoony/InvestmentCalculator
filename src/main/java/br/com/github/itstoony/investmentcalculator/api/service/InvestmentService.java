package br.com.github.itstoony.investmentcalculator.api.service;

import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentDTO;
import br.com.github.itstoony.investmentcalculator.api.model.dto.InvestmentFilterDTO;
import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import br.com.github.itstoony.investmentcalculator.api.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;

    public Investment save(Investment investment) {
        return repository.save(investment);
    }

    public Page<Investment> find(InvestmentFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    public Optional<Investment> findById(Long id) {
        return repository.findById(id);
    }

    public Investment update(Investment investment, InvestmentDTO update) {
        return null;
    }

    public void delete(Investment investment) {

    }
}
