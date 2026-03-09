package org.expenseincometracker.expenseincometracker.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.service.AdminUserService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminUserService.getAllUsers(page, size)
                )
        );
    }

    @GetMapping("/suspended")
    public ResponseEntity<?> getAllSuspendedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminUserService.getAllSUSPENDEDUsers(page, size)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminUserService.getUserById(id)
                )
        );
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<?> suspendUser(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success(
                        adminUserService.changeUserStatus(id)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        adminUserService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
