package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.ParentTransactionResponse;
import org.expenseincometracker.expenseincometracker.dto.response.TransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest request, User user);
    Page<ParentTransactionResponse> getParentTransactions(
            Authentication authentication,
            Pageable pageable
    );
}
