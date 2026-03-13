package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminTransactionResponse(
        String owner,
        Long transactionId,
        String userName,
        TransactionType transactionType,
        BigDecimal amount,
        String category,
        LocalDateTime date,
        UserStatus userStatus
) {
}
