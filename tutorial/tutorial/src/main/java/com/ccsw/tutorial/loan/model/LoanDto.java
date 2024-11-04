package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.util.Date;

public class LoanDto {
    private Long id;
    private Date date_loan;
    private Date date_return;
    private GameDto game;
    private CustomerDto customer;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate_loan() {
        return this.date_loan;
    }

    public void setDate_loan(Date date_loan) {
        this.date_loan = date_loan;
    }

    public Date getDate_return() {
        return this.date_return;
    }

    public void setDate_return(Date date_return) {
        this.date_return = date_return;
    }

    public CustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public GameDto getGame() {
        return this.game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

}
