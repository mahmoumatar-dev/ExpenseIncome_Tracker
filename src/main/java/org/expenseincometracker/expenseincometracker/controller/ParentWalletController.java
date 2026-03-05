package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.response.WalletResponse;
import org.expenseincometracker.expenseincometracker.service.ParentWalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent/wallets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Parent – Wallet Management", description = "APIs for managing wallets owned by the authenticated parent")
public class ParentWalletController {

    private final ParentWalletService walletService;

    @PostMapping
    @Operation(summary = "Create a new wallet", description = "Creates a new wallet for the authenticated parent. Optionally assigns children to the wallet.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Wallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or children do not belong to the parent"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PARENT role")
    })
    public ResponseEntity<?> createWallet(
            @RequestBody @Valid CreateWalletRequest request,
            Authentication authentication
    ) {
        WalletResponse response = walletService.createWallet(request, authentication);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(org.expenseincometracker.expenseincometracker.util.model.ApiResponse.success(response));
    }

    @PutMapping("/{walletId}")
    @Operation(summary = "Update an existing wallet", description = "Partially updates a wallet owned by the authenticated parent. All fields are optional.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or children do not belong to the parent"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PARENT role"),
            @ApiResponse(responseCode = "404", description = "Wallet not found or does not belong to the parent")
    })
    public ResponseEntity<?> updateWallet(
            @PathVariable Long walletId,
            @RequestBody @Valid UpdateWalletRequest request,
            Authentication authentication
    ) {
        WalletResponse response = walletService.updateWallet(walletId, request, authentication);
        return ResponseEntity.ok(org.expenseincometracker.expenseincometracker.util.model.ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all parent wallets", description = "Returns all wallets owned by the authenticated parent, including assigned children details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PARENT role")
    })
    public ResponseEntity<?> getParentWallets(Authentication authentication) {
        List<WalletResponse> wallets = walletService.getParentWallets(authentication);
        return ResponseEntity.ok(org.expenseincometracker.expenseincometracker.util.model.ApiResponse.success(wallets));
    }

    @DeleteMapping("/{walletId}")
    @Operation(summary = "Delete a wallet", description = "Deletes a wallet owned by the authenticated parent.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Wallet deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PARENT role"),
            @ApiResponse(responseCode = "404", description = "Wallet not found or does not belong to the parent")
    })
    public ResponseEntity<?> deleteWallet(
            @PathVariable Long walletId,
            Authentication authentication
    ) {
        walletService.deleteWallet(walletId, authentication);
        return ResponseEntity.noContent().build();
    }
}
