package org.expenseincometracker.expenseincometracker.dto.response;

import java.time.LocalDateTime;


public record CategoryResponse(
        Long id,
        String name,

        LocalDateTime createdAt
) {
}
