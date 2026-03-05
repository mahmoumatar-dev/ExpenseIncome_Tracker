package org.expenseincometracker.expenseincometracker.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(
        @NotEmpty String refreshToken
) {
}
