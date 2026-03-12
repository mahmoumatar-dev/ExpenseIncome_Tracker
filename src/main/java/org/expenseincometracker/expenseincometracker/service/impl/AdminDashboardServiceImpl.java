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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public AdminDashboardResponse getDashboardStatistics() {
        List<Role>roles=List.of(
                Role.ROLE_PARENT,
                Role.ROLE_CHILD
        );
        long totalUsers = userRepository.countParentAndChildrenUsers(roles);

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
    public List<MonthlyTransactionVolumeResponse> getTransactionsVolumeLast12Months() {

        LocalDateTime startDate = LocalDateTime.now().minusMonths(11);

        List<MonthlyTransactionVolumeResponse> dbResults =
                transactionRepository.findMonthlyTransactionVolumeSince(startDate);

        Map<String, Double> dbMap = dbResults.stream()
                .collect(Collectors.toMap(
                        r -> r.year() + "-" + r.month(),
                        MonthlyTransactionVolumeResponse::averageTransactionAmount
                ));

        List<MonthlyTransactionVolumeResponse> result = new ArrayList<>();

        LocalDate current = LocalDate.now().minusMonths(11);

        for(int i = 0; i < 12; i++){

            int year = current.getYear();
            int month = current.getMonthValue();

            String key = year + "-" + month;

            Double avgAmount = dbMap.getOrDefault(key, 0.0);

            result.add(new MonthlyTransactionVolumeResponse(year, month, avgAmount));

            current = current.plusMonths(1);
        }

        return result;
    }
    @Override
    public List<MonthlyUserRegistrationResponse> getMonthlyUserRegistrations(){
        List<Role> roles= List.of(
                Role.ROLE_PARENT,Role.ROLE_CHILD
        );
        LocalDateTime startDate = LocalDateTime.now().minusMonths(11);

        List<MonthlyUserRegistrationResponse> dbResults =
                userRepository.findMonthlyUserRegistrationsSince(roles,startDate);

        Map<String, Long> dbMap = dbResults.stream()
                .collect(Collectors.toMap(
                        r -> r.year() + "-" + r.month(),
                        MonthlyUserRegistrationResponse::registrations
                ));

        List<MonthlyUserRegistrationResponse> result = new ArrayList<>();

        LocalDate current = LocalDate.now().minusMonths(11);

        for(int i = 0; i < 12; i++){

            int year = current.getYear();
            int month = current.getMonthValue();

            String key = year + "-" + month;

            Long count = dbMap.getOrDefault(key, 0L);

            result.add(new MonthlyUserRegistrationResponse(year, month, count));

            current = current.plusMonths(1);
        }

        return result;
    }
}
