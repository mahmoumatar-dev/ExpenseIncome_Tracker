package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.dto.response.TransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.*;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.helper.BudgetHelper;
import org.expenseincometracker.expenseincometracker.helper.CategoryHelper;
import org.expenseincometracker.expenseincometracker.helper.TransactionHelper;
import org.expenseincometracker.expenseincometracker.helper.WalletHelper;
import org.expenseincometracker.expenseincometracker.repository.CategoryRepository;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.repository.WalletRepository;
import org.expenseincometracker.expenseincometracker.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final WalletHelper walletHelper;
    private final BudgetHelper budgetHelper;
    private final CategoryHelper categoryHelper;
    private final TransactionHelper transactionHelper;


    @Override
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request, User user) {

        if(user.getStatus()== UserStatus.SUSPENDED)
            throw new BusinessException("Your account is suspended you can't create any transaction");

        transactionHelper.validateTransactionTypeForUser(request.type(), user);

        Wallet wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        walletHelper.validateWalletAccess(wallet, user);

        Category category = null;
        if (request.type() == TransactionType.EXPENSE) {

            transactionHelper.validateChildSpendingLimit(user, request.amount());

            if (request.categoryId() == null) {
                throw new IllegalArgumentException("Category is required for expense transaction");
            }

            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            categoryHelper.validateCategoryAccess(category, wallet.getOwner());
            budgetHelper.validateBudget(category, request.amount(), wallet.getOwner());
        }

        walletHelper.updateWalletBalance(wallet, request);

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .type(request.type())
                .description(request.description())
                .wallet(wallet)
                .category(category)
                .createdBy(user)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return mapToResponse(saved);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),

                transaction.getWallet().getId(),
                transaction.getWallet().getName(),

                transaction.getCategory() != null ? transaction.getCategory().getId() : null,
                transaction.getCategory() != null ? transaction.getCategory().getName() : null,

                transaction.getCreatedAt()
        );
    }
}
