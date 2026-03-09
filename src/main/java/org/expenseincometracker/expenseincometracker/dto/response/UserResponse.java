package org.expenseincometracker.expenseincometracker.dto.response;

import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role user_role,
    UserStatus user_status,
    Long numOfTransaction,
    LocalDateTime date_joined

) {
}
