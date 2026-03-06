package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateBudgetRequest;
import org.expenseincometracker.expenseincometracker.dto.response.BudgetResponse;
import org.expenseincometracker.expenseincometracker.entity.Budget;
import org.expenseincometracker.expenseincometracker.entity.Category;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.repository.BudgetRepository;
import org.expenseincometracker.expenseincometracker.repository.CategoryRepository;
import org.expenseincometracker.expenseincometracker.service.BudgetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BudgetResponse createBudget(CreateBudgetRequest request, User parent) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getParent().getId().equals(parent.getId())) {
            throw new ResourceNotFoundException("Category not found or does not belong to you");
        }

        Budget budget = budgetRepository.findByCategoryIdAndParentId(category.getId(),parent.getId())
                .orElse(Budget.builder()
                        .category(category)
                        .parent(parent)
                        .build());

        budget.setMonthlyLimit(request.monthlyLimit());
        
        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponse(savedBudget);
    }

    @Override
    public List<BudgetResponse> getBudgets(User parent) {
        return budgetRepository.findByParentId(parent.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BudgetResponse mapToResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),budget.getCategory().getId(),budget.getCategory().getName(),budget.getMonthlyLimit(),budget.getCreatedAt()
        );
    }
}
