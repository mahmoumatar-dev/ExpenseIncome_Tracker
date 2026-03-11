package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.expenseincometracker.expenseincometracker.dto.response.ChildResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.NotFoundException;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.ParentChildrenManagementService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentChildrenManagementImpl implements ParentChildrenManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;
    private final UserHelper userHelper;

    @Override
    public void createChild(CreateChildRequest request, Authentication authentication) {

        User parent = userHelper.getAuthenticatedUser(authentication);

        if (parent.getRole() != Role.ROLE_PARENT) {
            throw new BusinessException("This user can't add children");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Child email already in use");
        }

        User child = new User();
        child.setName(request.name());
        child.setEmail(request.email());
        child.setPassword(passwordEncoder.encode(request.password()));
        child.setRole(Role.ROLE_CHILD);
        child.setParent(parent);
        child.setStatus(UserStatus.ACTIVE);
        child.setSpendingLimit(request.spendingLimit());

        userRepository.save(child);
    }

    @Override
    public List<ChildResponse> getChildren(Authentication authentication) {
        User parent =userHelper.getAuthenticatedUser(authentication);

        List<User> children = userRepository.findByParentId(parent.getId());

        return children.stream().map(child -> {
            BigDecimal spent = transactionRepository.sumChildExpensesThisMonth(child.getId());
            return new ChildResponse(
                    child.getId(),
                    child.getName(),
                    child.getEmail(),
                    child.getStatus(),
                    child.getSpendingLimit(),
                    spent
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChildResponse updateChildStatus(Long childId, Authentication authentication) {
        User parent =userHelper.getAuthenticatedUser(authentication);

        User child = userRepository.findById(childId)
                .orElseThrow(() -> new NotFoundException("Child not found"));

        if (!child.getParent().getId().equals(parent.getId())) {
            throw new BusinessException("You can only update your own children");
        }

        child.setStatus(
                child.getStatus()==UserStatus.ACTIVE?
                        UserStatus.SUSPENDED:
                        UserStatus.ACTIVE);
        child = userRepository.save(child);

        BigDecimal spent = transactionRepository.sumChildExpensesThisMonth(child.getId());

        return new ChildResponse(
                child.getId(),
                child.getName(),
                child.getEmail(),
                child.getStatus(),
                child.getSpendingLimit(),
                spent
        );
    }
}
