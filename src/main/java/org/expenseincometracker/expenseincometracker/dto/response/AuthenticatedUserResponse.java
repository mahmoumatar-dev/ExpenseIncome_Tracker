package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;

public record AuthenticatedUserResponse(
        Long id,
        String name,
        String email,
        Role role,
        UserStatus status
) {
}
