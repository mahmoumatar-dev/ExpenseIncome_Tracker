package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateBudgetRequest;
import org.expenseincometracker.expenseincometracker.dto.response.BudgetResponse;
import org.expenseincometracker.expenseincometracker.entity.User;

import java.util.List;

public interface BudgetService {
    BudgetResponse createBudget(CreateBudgetRequest request, User parent);
    List<BudgetResponse> getBudgets(User parent);
}
