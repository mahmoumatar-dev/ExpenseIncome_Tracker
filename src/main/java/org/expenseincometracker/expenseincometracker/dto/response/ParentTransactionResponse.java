package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParentTransactionResponse(
        String owner,
        LocalDateTime date,
        String category,
        String wallet,
        TransactionType type,
        BigDecimal amount,
        String description
) {}
