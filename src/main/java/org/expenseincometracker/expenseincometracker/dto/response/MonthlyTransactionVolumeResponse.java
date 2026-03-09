package org.expenseincometracker.expenseincometracker.dto.response;

public record MonthlyTransactionVolumeResponse(
        Integer year,
        Integer month,
        Double averageTransactionAmount
) {
}
