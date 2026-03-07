package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.UserStatus;

import java.math.BigDecimal;

public record ChildResponse(
        Long id,
        String name,
        String email,
        UserStatus status,
        BigDecimal spendingLimit,
        BigDecimal totalSpentThisMonth
) {}