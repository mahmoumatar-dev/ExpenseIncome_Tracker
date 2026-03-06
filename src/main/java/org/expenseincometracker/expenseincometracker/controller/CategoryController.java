package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateCategoryRequest;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateCategoryRequest;
import org.expenseincometracker.expenseincometracker.dto.response.CategoryResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.CategoryService;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category Management", description = "APIs for managing categories created by the authenticated parent")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            Authentication authentication) {
        User parent =findParentByEmail(authentication.getName());
        CategoryResponse response = categoryService.createCategory(request, parent);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
    public ResponseEntity<?> getCategories(Authentication authentication) {
        User parent =findParentByEmail(authentication.getName());
        List<CategoryResponse> responses = categoryService.getCategories(parent);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
    public ResponseEntity<?> getCategoriesSummary(
            Authentication authentication
    ) {
        User parent =findParentByEmail(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(
                categoryService.getCategoryBudgetSummary(parent)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PARENT')")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request,
            Authentication authentication
    ) {
        User parent =findParentByEmail(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(
                categoryService.updateCategory(id, request, parent)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PARENT')")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User parent =findParentByEmail(authentication.getName());
        categoryService.deleteCategory(id, parent);
        return ResponseEntity.noContent().build();
    }

    // helper method
    User findParentByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
