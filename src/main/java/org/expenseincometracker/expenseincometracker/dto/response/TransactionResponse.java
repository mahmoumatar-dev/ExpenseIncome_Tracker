package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record TransactionResponse (
        Long id,
        BigDecimal amount,
        TransactionType type,
        String description,

        Long walletId,
        String walletName,

        Long categoryId,
        String categoryName,

        LocalDateTime createdAt
){
}
