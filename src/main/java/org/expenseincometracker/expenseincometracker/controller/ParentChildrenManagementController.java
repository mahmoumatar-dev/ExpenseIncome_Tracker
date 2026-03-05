package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.expenseincometracker.expenseincometracker.service.ParentChildrenManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/parent/child")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Parent – Children Management", description = "APIs for managing children owned by the authenticated parent")
public class ParentChildrenManagementController {

    private final ParentChildrenManagementService parentService;

    @PostMapping
    @Operation(summary = "Create a new children", description = "Creates a new children account for the authenticated parent.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Children account created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PARENT role")
    })
    public ResponseEntity<?> createChild(
            @RequestBody @Valid CreateChildRequest request,
            Authentication authentication
    ) {
        parentService.createChild(request, authentication);
        return ResponseEntity.ok("Child account created successfully");
    }

}
