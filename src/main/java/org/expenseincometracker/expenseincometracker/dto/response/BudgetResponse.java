package org.expenseincometracker.expenseincometracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record BudgetResponse(
        Long id,
        Long categoryId,
        String categoryName,
        BigDecimal monthlyLimit,
        LocalDateTime createdAt
) {

}
