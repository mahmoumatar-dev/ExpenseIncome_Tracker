package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.TransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest request, User user);
}
