package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateProfileRequest;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.ParentProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParentProfileServiceImpl implements ParentProfileService {

    private final UserHelper userHelper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateProfile(Authentication authentication, UpdateProfileRequest request) {

        User user =userHelper.getAuthenticatedUser(authentication);

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.email() != null && !request.email().isBlank()) {
            if (userRepository.existsByEmail(request.email())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(request.email());
        }

        if (request.newPassword() != null && !request.newPassword().isBlank()) {
            if (request.currentPassword() == null ||
                    !passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.newPassword()));
        }
        userRepository.save(user);
    }
}
