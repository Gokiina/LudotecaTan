package com.ccsw.tutorial.loan;

public class LoanConflictException extends RuntimeException {

    public LoanConflictException(String message) {
        super(message);
        System.out.println(message);
    }
}

