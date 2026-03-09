package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.*;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.service.ParentInsightService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentInsightServiceImpl implements ParentInsightService {

    private final TransactionRepository transactionRepository;
    private final UserHelper userHelper;

    @Override
    public ParentDashboardOverviewResponse getOverview(Authentication authentication) {
        Long parentId = userHelper.getAuthenticatedUserId(authentication);

        BigDecimal averageMonthlySpending = transactionRepository.averageMonthlySpending(parentId);
        if (averageMonthlySpending == null) {
            averageMonthlySpending = BigDecimal.ZERO;
        }
        averageMonthlySpending = averageMonthlySpending.setScale(2, RoundingMode.HALF_UP);

        String topCategory = transactionRepository.findTopCategory(parentId);
        if (topCategory == null) {
            topCategory = "N/A";
        }

        BigDecimal totalChildrenSpending = transactionRepository.sumChildrenSpendingByParent(parentId);
        if (totalChildrenSpending == null) {
            totalChildrenSpending = BigDecimal.ZERO;
        }

        return new ParentDashboardOverviewResponse(averageMonthlySpending, topCategory, totalChildrenSpending);
    }


    @Override
    public List<CategorySpendingResponse> getSpendingByCategory(Authentication authentication) {
        Long parentId = userHelper.getAuthenticatedUserId(authentication);
        return transactionRepository.spendingByCategory(parentId);
    }


    @Override
    public List<IncomeExpenseResponse> getIncomeVsExpense (Authentication authentication){
        Long parentId = userHelper.getAuthenticatedUserId(authentication);
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return transactionRepository.incomeVsExpense(parentId, sixMonthsAgo);
    }


    @Override
    public List<ChildSpendingResponse> getChildrenAnalysis (Authentication authentication){
        Long parentId = userHelper.getAuthenticatedUserId(authentication);
        return  transactionRepository.sumChildrenExpensesThisMonth(parentId);
    }


}
