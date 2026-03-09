package org.expenseincometracker.expenseincometracker.dto.response;

public record MonthlyUserRegistrationResponse(
        Integer year,
        Integer month,
        Long registrations
) {
}
