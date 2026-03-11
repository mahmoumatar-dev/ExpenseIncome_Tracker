package org.expenseincometracker.expenseincometracker.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateProfileRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthenticatedUserResponse;
import org.expenseincometracker.expenseincometracker.service.ParentProfileService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Parent – Profile Management", description = "APIs for managing authenticated parent profile")
public class ParentProfileController {

    private final ParentProfileService parentProfileService;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {

        AuthenticatedUserResponse userResponse = parentProfileService.updateProfile(authentication, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        userResponse
                )
        );
    }

}
