package org.expenseincometracker.expenseincometracker.helper;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class TransactionHelper {

    private final TransactionRepository transactionRepository;

    public void validateTransactionTypeForUser(TransactionType type, User user)  {
        if (isChild(user) && type == TransactionType.INCOME) {
            throw new AccessDeniedException("Child users cannot create income transactions");
        }
    }
    private boolean isChild(User user) {
        return user.getRole() == Role.ROLE_CHILD;
    }

    public void validateChildSpendingLimit(User user, BigDecimal amount) {
        if (user.getRole() != Role.ROLE_CHILD) {
            return;
        }
        BigDecimal limit = user.getSpendingLimit();
        if (limit == null) {
            return;
        }
        BigDecimal spentThisMonth =
                transactionRepository.sumChildExpensesThisMonth(user.getId());
        BigDecimal total = spentThisMonth.add(amount);
        if (total.compareTo(limit) > 0) {
            throw new BusinessException("Child spending limit exceeded");
        }
    }
}
