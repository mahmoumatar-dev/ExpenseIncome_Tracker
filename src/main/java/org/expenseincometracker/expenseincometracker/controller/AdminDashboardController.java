package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.service.AdminDashboardService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboardStatistics(){
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminDashboardService.getDashboardStatistics()
                )
        );
    }

    @GetMapping("/user-distribution")
    public ResponseEntity<?> getUserDistribution() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminDashboardService.getUserDistribution()
                )
        );
    }

    @GetMapping("/recent-users")
    public ResponseEntity<?> getRecentUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminDashboardService.getRecentUsers(page, size)
                )
        );
    }

    @GetMapping("/transactions-volume")
    public ResponseEntity<?> getTransactionsVolume(){
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminDashboardService.getMonthlyTransactionVolume()
                )
        );
    }

    @GetMapping("/user-registrations")
    public ResponseEntity<?> getUserRegistrations(){
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminDashboardService.getMonthlyUserRegistrations()
                )
        );
    }
}