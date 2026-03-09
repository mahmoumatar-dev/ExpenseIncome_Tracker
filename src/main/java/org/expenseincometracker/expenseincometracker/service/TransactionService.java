package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.ParentTransactionResponse;
import org.expenseincometracker.expenseincometracker.dto.response.TransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest request, User user);
    Page<ParentTransactionResponse> getParentTransactions(
            Authentication authentication,
            Pageable pageable
    );

    Page<ParentTransactionResponse> getChildUserTransactions(
            Authentication authentication,
            Pageable pageable
    );
}
