package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.AdminTransactionResponse;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.service.AdminTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminTransactionServiceImpl implements AdminTransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Page<AdminTransactionResponse> getAllTransactions(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );
        List<Role> roles = List.of(Role.ROLE_PARENT, Role.ROLE_CHILD);

        return transactionRepository.findAllParentAndChildTransactions(roles,pageable);
    }
}
