package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateCategoryRequest;
import org.expenseincometracker.expenseincometracker.dto.response.CategoryBudgetSummaryResponse;
import org.expenseincometracker.expenseincometracker.dto.response.CategoryResponse;
import org.expenseincometracker.expenseincometracker.entity.Budget;
import org.expenseincometracker.expenseincometracker.entity.Category;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.repository.BudgetRepository;
import org.expenseincometracker.expenseincometracker.repository.CategoryRepository;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.service.CategoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request, User parent) {

        Category category = Category.builder()
                .name(request.name())
                .parent(parent)
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getCategories(User user) {
        Long parentId = user.getParent() != null ? user.getParent().getId() : user.getId();
        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryBudgetSummaryResponse> getCategoryBudgetSummary(User user) {

        Long parentId = user.getParent() != null ? user.getParent().getId() : user.getId();
        List<Category> categories = categoryRepository.findByParentId(parentId);

        return categories.stream().map(category -> {
            Budget budget = budgetRepository
                    .findByCategoryIdAndParentId(category.getId(), parentId)
                    .orElse(null);
            BigDecimal limit = budget != null ? budget.getMonthlyLimit() : BigDecimal.ZERO;

            BigDecimal used = transactionRepository
                    .sumSpentByCategory(category.getId(), parentId);

            BigDecimal percentage = BigDecimal.ZERO;
            if (limit.compareTo(BigDecimal.ZERO) > 0) {
                percentage = used
                        .multiply(BigDecimal.valueOf(100))
                        .divide(limit, 2, RoundingMode.HALF_UP);
            }

            return new CategoryBudgetSummaryResponse(
                    category.getId(),
                    category.getName(),
                    limit,
                    used,
                    percentage
            );

        }).toList();
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName() ,category.getCreatedAt());
    }
}
