package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.*;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.AdminDashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public AdminDashboardResponse getDashboardStatistics() {
        long totalUsers = userRepository.count();

        long activeChildren =
                userRepository.countByRoleAndStatus(Role.ROLE_CHILD, UserStatus.ACTIVE);

        long activeParents =
                userRepository.countByRoleAndStatus(Role.ROLE_PARENT, UserStatus.ACTIVE);

        BigDecimal totalAmount =
                transactionRepository.getTotalTransactionAmount();

        return new AdminDashboardResponse(
                totalUsers,
                activeChildren,
                activeParents,
                totalAmount
        );
    }

    @Override
    public UserDistributionResponse getUserDistribution() {
        long parentCount = userRepository.countByRole(Role.ROLE_PARENT);
        long childCount = userRepository.countByRole(Role.ROLE_CHILD);

        long total = parentCount + childCount;

        double parentPercentage = total == 0 ? 0 : ((double) parentCount / total) * 100;
        double childPercentage = total == 0 ? 0 : ((double) childCount / total) * 100;

        return new UserDistributionResponse(
                parentCount,
                childCount,
                parentPercentage,
                childPercentage
        );
    }

    @Override
    public Page<RecentUserResponse> getRecentUsers(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        List<Role> roles= List.of(
                Role.ROLE_PARENT,Role.ROLE_CHILD
        );

        return userRepository.findRecentUsers(roles,pageable);
    }

    @Override
    public List<MonthlyTransactionVolumeResponse> getMonthlyTransactionVolume() {
        return transactionRepository.getMonthlyTransactionVolume();
    }

    @Override
    public List<MonthlyUserRegistrationResponse> getMonthlyUserRegistrations(){
        List<Role> roles= List.of(
                Role.ROLE_PARENT,Role.ROLE_CHILD
        );
        return userRepository.getMonthlyUserRegistrations(roles);
    }
}
