package br.com.github.itstoony.investmentcalculator.api.repository;

import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    Page<Investment> findByDate(LocalDateTime dateTime, Pageable pageable);

}
