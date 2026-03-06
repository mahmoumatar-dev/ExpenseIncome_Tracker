package org.expenseincometracker.expenseincometracker.dto.response;

import java.math.BigDecimal;

public record CategoryBudgetSummaryResponse(
        Long categoryId,
        String categoryName,
        BigDecimal budgetLimit,
        BigDecimal usedAmount,
        BigDecimal usedPercentage
) {
}
