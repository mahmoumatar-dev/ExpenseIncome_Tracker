package org.expenseincometracker.expenseincometracker.helper;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.entity.Budget;
import org.expenseincometracker.expenseincometracker.entity.Category;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.repository.BudgetRepository;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BudgetHelper {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public void validateBudget(Category category, BigDecimal amount, User parent) {
        Budget budget = budgetRepository
                .findByCategoryIdAndParentId(category.getId(), parent.getId())
                .orElse(null);
        if (budget == null) {
            throw new BusinessException("Something wrong!!!!");
        }
        BigDecimal spent = transactionRepository
                .sumCategoryExpensesThisMonth(category.getId(), parent.getId());

        BigDecimal newTotal = spent.add(amount);
        if (newTotal.compareTo(budget.getMonthlyLimit()) > 0) {
            throw new IllegalStateException("Budget exceeded for category: " + category.getName());
        }
    }
}
