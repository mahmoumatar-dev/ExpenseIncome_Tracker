package org.expenseincometracker.expenseincometracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.CategorySpendingResponse;
import org.expenseincometracker.expenseincometracker.dto.response.ChildDashboardOverviewResponse;
import org.expenseincometracker.expenseincometracker.service.ChildrenInsightService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/child/insights")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CHILD')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Child – Insights Analytics")
public class ChildrenInsightController {

    private final ChildrenInsightService childrenInsightService;

    @GetMapping("/overview")
    @Operation(summary = "Children overview insights")
    public ResponseEntity<?> getOverview(
            Authentication authentication
    ) {
        ChildDashboardOverviewResponse response = childrenInsightService.getDashboard(authentication);
        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

    @GetMapping("/spending-by-category")
    @Operation(summary = "Spending by category",
            description = "Returns expense transactions grouped by category with summed amounts.")
    public ResponseEntity<?> getSpendingByCategory(
            Authentication authentication
    ) {
        List<CategorySpendingResponse> response = childrenInsightService.getCategorySpending(authentication);
        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }

}
