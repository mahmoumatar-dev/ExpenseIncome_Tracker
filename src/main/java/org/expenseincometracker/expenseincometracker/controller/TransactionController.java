package org.expenseincometracker.expenseincometracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.ParentTransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.TransactionService;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transaction Management", description = "APIs for managing expense or income transactions for by the authenticated parent or its children")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('PARENT', 'CHILD')")
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

    @PreAuthorize("hasAnyRole('PARENT')")
    @GetMapping("/parent")
    public ResponseEntity<?> getParentTransactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(
                page,
                6,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<ParentTransactionResponse> transactions =
                transactionService.getParentTransactions(authentication, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasAnyRole('CHILD')")
    @GetMapping("/child")
    public ResponseEntity<?> getChildTransactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(
                page,
                6,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<ParentTransactionResponse> transactions =
                transactionService.getChildUserTransactions(authentication, pageable);
        return ResponseEntity.ok(transactions);
    }


}
