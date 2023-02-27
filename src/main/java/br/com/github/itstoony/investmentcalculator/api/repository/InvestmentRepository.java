package br.com.github.itstoony.investmentcalculator.api.repository;

import br.com.github.itstoony.investmentcalculator.api.model.entity.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT I FROM Investment I WHERE I.date BETWEEN :begin AND :end")
    Page<Investment> findByDate(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end, Pageable pageable);

}
