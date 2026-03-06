package org.expenseincometracker.expenseincometracker.exception;

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
