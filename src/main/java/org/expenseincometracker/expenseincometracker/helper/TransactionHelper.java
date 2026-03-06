package org.expenseincometracker.expenseincometracker.helper;

import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


@Component
public class TransactionHelper {

    public void validateTransactionTypeForUser(TransactionType type, User user)  {
        if (isChild(user) && type == TransactionType.INCOME) {
            throw new AccessDeniedException("Child users cannot create income transactions");
        }
    }

    private boolean isChild(User user) {
        return user.getRole() == Role.ROLE_CHILD;
    }
}
