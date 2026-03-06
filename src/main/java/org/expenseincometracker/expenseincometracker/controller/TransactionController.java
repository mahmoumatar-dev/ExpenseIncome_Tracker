package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.TransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.TransactionService;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transaction Management", description = "APIs for managing expense or income transactions for by the authenticated parent or its children")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request,
            Authentication authentication) {
            
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                ApiResponse.success(
                        transactionService.createTransaction(request, user)
                )
        );
    }

}
