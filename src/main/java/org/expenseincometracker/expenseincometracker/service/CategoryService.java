package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateCategoryRequest;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateCategoryRequest;
import org.expenseincometracker.expenseincometracker.dto.response.CategoryBudgetSummaryResponse;
import org.expenseincometracker.expenseincometracker.dto.response.CategoryResponse;
import org.expenseincometracker.expenseincometracker.entity.User;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request, User parent);
    List<CategoryResponse> getCategories(User user);
    List<CategoryBudgetSummaryResponse> getCategoryBudgetSummary(User user);
    CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request, User parent);
    void deleteCategory(Long categoryId, User parent);
}
