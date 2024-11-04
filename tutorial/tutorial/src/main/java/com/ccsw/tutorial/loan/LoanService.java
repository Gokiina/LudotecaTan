package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.LoanSearchDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.springframework.data.domain.Page;

public interface LoanService {
    Loan get(Long id);

    void save(Long id, LoanDto dto);

    void delete(Long id) throws Exception;

    Page<Loan> findPage(LoanSearchDto dto);
}
