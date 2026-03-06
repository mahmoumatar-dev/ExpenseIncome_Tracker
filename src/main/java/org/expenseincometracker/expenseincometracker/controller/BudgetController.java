package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateBudgetRequest;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.BudgetService;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('PARENT')")
@Tag(name = "Parent – Budget Management", description = "APIs for managing budget for specific category owned by authenticated parents")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createBudget(
            @Valid @RequestBody CreateBudgetRequest request,
            Authentication authentication) {
            
        User parent = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                ApiResponse.success(
                        budgetService.createBudget(request, parent
                        )
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> getBudgets(Authentication authentication) {
        User parent = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                ApiResponse.success(
                        budgetService.getBudgets(parent)
                )
        );
    }
}
