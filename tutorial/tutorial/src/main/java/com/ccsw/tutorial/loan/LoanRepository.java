package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    @Override
    @EntityGraph(attributePaths = { "customer", "game" })
    List<Loan> findAll(Specification<Loan> spec);

    Page<Loan> findByGameId(Long idGame, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.game.id = :gameId AND l.date_loan <= :endDate AND l.date_return >= :startDate")
    List<Loan> findLoansWithDateRange(@Param("gameId") Long gameId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT l FROM Loan l WHERE l.customer.id = :customerId AND " + "l.date_loan <= :endDate AND l.date_return >= :startDate")
    List<Loan> findLoansForClientWithDateRange(@Param("customerId") Long customerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

