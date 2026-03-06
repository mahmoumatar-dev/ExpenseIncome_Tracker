package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateCategoryRequest;
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
        
        User parent = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CategoryResponse response = categoryService.createCategory(request, parent);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
    public ResponseEntity<?> getCategories(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<CategoryResponse> responses = categoryService.getCategories(user);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
    public ResponseEntity<?> getCategoriesSummary(
            Authentication authentication
    ) {
        User parent = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(categoryService.getCategoryBudgetSummary(parent));
    }
}
