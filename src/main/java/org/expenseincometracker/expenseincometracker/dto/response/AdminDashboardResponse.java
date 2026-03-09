package org.expenseincometracker.expenseincometracker.dto.response;

import java.math.BigDecimal;

public record AdminDashboardResponse(
        Long totalUsers,
        Long activeChildren,
        Long activeParents,
        BigDecimal totalTransactionAmount
) {
}
