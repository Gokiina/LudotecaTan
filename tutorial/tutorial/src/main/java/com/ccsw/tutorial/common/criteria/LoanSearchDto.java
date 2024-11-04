package com.ccsw.tutorial.common.criteria;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.util.Date;

public class LoanSearchDto {

    private Date date_loan;
    private Long customer;
    private Long game;
    private PageableRequest pageable;

    public Date getDate_loan() {
        return date_loan;
    }

    public void setDate_loan(Date date_loan) {
        this.date_loan = date_loan;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getGame() {
        return game;
    }

    public void setGame(Long game) {
        this.game = game;
    }

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
