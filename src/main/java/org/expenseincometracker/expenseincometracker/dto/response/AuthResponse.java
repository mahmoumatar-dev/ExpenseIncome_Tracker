package org.expenseincometracker.expenseincometracker.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
