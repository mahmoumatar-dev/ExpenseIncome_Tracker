package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.expenseincometracker.expenseincometracker.service.ParentChildrenManagementService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/parent/child")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Parent – Children Management", description = "APIs for managing children owned by the authenticated parent")
public class ParentChildrenManagementController {

    private final ParentChildrenManagementService parentService;

    @PostMapping
    public ResponseEntity<?> createChild(
            @RequestBody @Valid CreateChildRequest request,
            Authentication authentication
    ) {
        parentService.createChild(request, authentication);
        return ResponseEntity.ok("Child account created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getChildren(Authentication authentication) {
        return ResponseEntity.ok(
                ApiResponse.success(
                parentService.getChildren(authentication)
        )
        );
    }

    @PutMapping("/{childId}/status")
    public ResponseEntity<?> updateChildStatus(
            @PathVariable Long childId,
            Authentication authentication
    ) {
        parentService.updateChildStatus(childId, authentication);
        return ResponseEntity.ok("Child status updated successfully");
    }

}
