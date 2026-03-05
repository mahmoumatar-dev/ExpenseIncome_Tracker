package org.expenseincometracker.expenseincometracker.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.LoginRequest;
import org.expenseincometracker.expenseincometracker.dto.request.RegisterRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthResponse;
import org.expenseincometracker.expenseincometracker.entity.RefreshToken;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.security.JwtService;
import org.expenseincometracker.expenseincometracker.service.AuthService;
import org.expenseincometracker.expenseincometracker.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BusinessException("Invalid email or password");
        }

        UserDetails user =
                userDetailsService.loadUserByUsername(request.email());

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Override
    @Transactional
    public void registerParent(RegisterRequest request) {
        register(request, Role.ROLE_PARENT);
    }

    @Override
    public void registerAdmin(RegisterRequest request) {
        register(request,Role.ROLE_ADMIN);
    }

    public void register(RegisterRequest request,Role role){
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use");
        }

        if (!request.password().equals(request.confirmPassword())){
            throw new BusinessException("Passwords are not match");
        }

        User user = new User();
        user.setName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
