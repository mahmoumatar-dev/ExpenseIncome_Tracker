package org.expenseincometracker.expenseincometracker.helper;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateTransactionRequest;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.NotFoundException;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class WalletHelper {

    private final UserRepository userRepository;

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
            wallet.addBalance(request.amount());
        } else {
            wallet.deductBalance(request.amount());
        }
    }

    public List<User> resolveAndValidateChildren(List<Long> childrenIds, User parent) {
        if (childrenIds == null || childrenIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<User> children = userRepository.findAllById(childrenIds);

        if (children.size() != childrenIds.size()) {
            throw new NotFoundException("One or more child IDs are invalid");
        }

        for (User child : children) {
            if (child.getRole() != Role.ROLE_CHILD) {
                throw new BusinessException("User with ID " + child.getId() + " is not a child account");
            }
            if (child.getParent() == null || !child.getParent().getId().equals(parent.getId())) {
                throw new BusinessException("Child with ID " + child.getId() + " does not belong to you");
            }
        }

        return children;
    }

}
