package org.expenseincometracker.expenseincometracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateChildRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
