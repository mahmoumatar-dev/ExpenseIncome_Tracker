package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.CategorySpendingResponse;
import org.expenseincometracker.expenseincometracker.dto.response.ChildDashboardOverviewResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.repository.WalletRepository;
import org.expenseincometracker.expenseincometracker.service.ChildrenInsightService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChildrenInsightServiceImpl implements ChildrenInsightService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserHelper userHelper;

    @Override
    public ChildDashboardOverviewResponse getDashboard(Authentication authentication) {
        User child = userHelper.getAuthenticatedUser(authentication);
        List<Wallet> wallets = walletRepository.findByAssignedChildren_Id(child.getId());

        if (wallets.isEmpty()) {
            throw new ResourceNotFoundException("No wallet assigned to this child");
        }

        Wallet wallet = wallets.get(0);

        BigDecimal totalSpent =
                transactionRepository.getTotalAmountByCreatedBy_Id(child.getId());

        BigDecimal remaining = child.getSpendingLimit().subtract(totalSpent);

        return new ChildDashboardOverviewResponse(
                wallet.getBalance(),
                child.getSpendingLimit(),
                totalSpent,
                remaining
        );
    }

    @Override
    public List<CategorySpendingResponse> getCategorySpending(Authentication authentication) {
        Long childId = userHelper.getAuthenticatedUserId(authentication);
        return transactionRepository.getChildSpendingByCategory(childId);
    }

}
