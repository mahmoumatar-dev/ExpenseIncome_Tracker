package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.Role;

import java.time.LocalDateTime;

public record RecentUserResponse(
        String name,
        String email,
        LocalDateTime dateRegistered,
        Role role
) {
}
