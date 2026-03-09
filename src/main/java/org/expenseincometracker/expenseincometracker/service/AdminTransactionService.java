package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.response.AdminTransactionResponse;
import org.springframework.data.domain.Page;

public interface AdminTransactionService {
    Page<AdminTransactionResponse> getAllTransactions(int page, int size);
}
