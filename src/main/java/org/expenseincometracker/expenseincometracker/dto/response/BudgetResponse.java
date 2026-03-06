package org.expenseincometracker.expenseincometracker.dto.response;

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
