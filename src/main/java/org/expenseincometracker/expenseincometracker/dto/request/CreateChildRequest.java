package org.expenseincometracker.expenseincometracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateChildRequest(
        @NotBlank String fullName,

        @Email
        @NotBlank String email,

        @NotBlank String password,

        @Positive
        BigDecimal spendingLimit
) {
}
