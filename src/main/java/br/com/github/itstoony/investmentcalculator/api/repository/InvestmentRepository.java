package br.com.github.itstoony.investmentcalculator.api.repository;

import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

}
