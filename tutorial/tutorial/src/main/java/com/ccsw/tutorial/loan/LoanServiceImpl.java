package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.LoanSearchDto;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.customer.CustomerService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    CustomerService customerService;

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {
        List<Specification<Loan>> specs = new ArrayList<>();

        if (dto.getGame() != null) {
            specs.add(new LoanSpecification(new SearchCriteria("game.id", ":", dto.getGame())));
        }
        if (dto.getCustomer() != null) {
            specs.add(new LoanSpecification(new SearchCriteria("customer.id", ":", dto.getCustomer())));
        }

        if (dto.getDate_loan() != null) {
            LocalDate loanDate = dto.getDate_loan().toInstant().atZone(ZoneId.of("CET")).toLocalDate();

            specs.add((root, query, criteriaBuilder) -> {
                return criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("date_loan"), loanDate.atTime(23, 59, 59)), // Fecha de inicio antes del final del día
                        criteriaBuilder.greaterThanOrEqualTo(root.get("date_return"), loanDate.atTime(0, 0, 0)) // Fecha de fin después del inicio del día
                );
            });

            System.out.println("LoanServiceImpl - Date: " + loanDate);
        }

        Specification<Loan> finalSpec = specs.stream().reduce(Specification::and).orElse(null);

        if (finalSpec == null) {
            return this.loanRepository.findAll(dto.getPageable().getPageable());
        }

        return this.loanRepository.findAll(finalSpec, dto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            Optional<Loan> existingLoan = this.loanRepository.findById(id);
            loan = existingLoan.orElse(new Loan());
        }

        BeanUtils.copyProperties(dto, loan, "id", "game", "customer");
        if (dto.getGame() != null) {
            loan.setGame(gameService.get(dto.getGame().getId()));
        }
        if (dto.getCustomer() != null) {
            loan.setCustomer(customerService.get(dto.getCustomer().getId()));
        }
        validateLoan(loan);

        this.loanRepository.save(loan);
    }

    public void validateLoan(Loan loan) throws LoanConflictException {
        Date startDate = loan.getDate_loan();
        Date endDate = loan.getDate_return();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Verificación de conflictos para el juego
        List<Loan> gameConflicts = loanRepository.findLoansWithDateRange(loan.getGame().getId(), startDate, endDate);
        for (Loan conflictLoan : gameConflicts) {
            // Si el juego está reservado por otro cliente
            if (!conflictLoan.getCustomer().getId().equals(loan.getCustomer().getId())) {
                throw new LoanConflictException("El juego " + loan.getGame().getTitle() + " ya está en préstamo en la fecha seleccionada " + conflictLoan.getDate_loan() + " a " + conflictLoan.getDate_return());
            }

            // Si el mismo cliente intenta reservar el mismo juego
            if (conflictLoan.getCustomer().getId().equals(loan.getCustomer().getId())) {
                throw new LoanConflictException(
                        "El cliente " + loan.getCustomer().getName() + " ya tiene reservado el juego " + loan.getGame().getTitle() + " en las fechas seleccionadas " + startDate.toString() + " a " + endDate.toString());
            }
        }

        // Verificación para el cliente y préstamos activos
        List<Loan> clientConflicts = loanRepository.findLoansForClientWithDateRange(loan.getCustomer().getId(), startDate, endDate);
        long activeLoansCount = clientConflicts.stream().filter(l -> !l.getId().equals(loan.getId())).count();

        if (activeLoansCount >= 2) {
            throw new LoanConflictException("El cliente " + loan.getCustomer().getName() + " ya tiene 2 préstamos en las fechas seleccionadas");
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Loan does not exist");
        }
        this.loanRepository.deleteById(id);
    }

    @Override
    public Loan get(Long id) {
        return this.loanRepository.findById(id).orElse(null);
    }
}
