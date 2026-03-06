package org.expenseincometracker.expenseincometracker.helper;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@RequiredArgsConstructor
public class WalletHelper {

    public void validateWalletAccess(Wallet wallet, User user) {
        if (user.getRole() == Role.ROLE_PARENT) {
            if (!wallet.getOwner().getId().equals(user.getId())) {
                throw new AccessDeniedException("You don't own this wallet");
            }
        } else {
            boolean assigned = wallet.getAssignedChildren()
                    .stream()
                    .anyMatch(child -> child.getId().equals(user.getId()));
            if (!assigned) {
                throw new AccessDeniedException("Wallet not assigned to you");
            }
        }
    }

    public void updateWalletBalance(Wallet wallet, CreateTransactionRequest request) {
        if (request.type() == TransactionType.INCOME) {
            wallet.setBalance(wallet.getBalance().add(request.amount()));
        } else {
            if (wallet.getBalance().compareTo(request.amount()) < 0) {
                throw new IllegalStateException("Insufficient wallet balance");
            }
            wallet.setBalance(wallet.getBalance().subtract(request.amount()));
        }
    }

}
