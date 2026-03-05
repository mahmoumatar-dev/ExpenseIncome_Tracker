package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.NotFoundException;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.ParentChildrenManagementService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentChildrenManagementImpl implements ParentChildrenManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createChild(CreateChildRequest request, Authentication authentication) {

        String parentEmail = authentication.getName();
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new NotFoundException("Parent not found"));

        if (parent.getRole() != Role.ROLE_PARENT) {
            throw new BusinessException("This user can't add children");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Child email already in use");
        }

        User child = new User();
        child.setName(request.fullName());
        child.setEmail(request.email());
        child.setPassword(passwordEncoder.encode(request.password()));
        child.setRole(Role.ROLE_CHILD);
        child.setParent(parent);
        child.setStatus(UserStatus.ACTIVE);

        userRepository.save(child);
    }
}
