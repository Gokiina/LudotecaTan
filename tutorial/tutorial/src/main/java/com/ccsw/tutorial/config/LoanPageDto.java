package com.ccsw.tutorial.config;

import com.ccsw.tutorial.loan.model.LoanDto;

import java.util.List;

public class LoanPageDto {
    private List<LoanDto> loans;
    private int totalPages;
    private long totalElements;
    
    public List<LoanDto> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanDto> loans) {
        this.loans = loans;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}

