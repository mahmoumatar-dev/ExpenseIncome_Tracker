package org.expenseincometracker.expenseincometracker.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.RefreshTokenRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthResponse;
import org.expenseincometracker.expenseincometracker.entity.RefreshToken;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.NotFoundException;
import org.expenseincometracker.expenseincometracker.repository.RefreshTokenRepository;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.security.JwtService;
import org.expenseincometracker.expenseincometracker.service.RefreshTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public RefreshToken createRefreshToken(String userEmail) {
        RefreshToken refreshToken = new RefreshToken();

        User user = userRepository.findByEmail(userEmail).orElseThrow(
                ()-> new NotFoundException("User not found")
        );
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                Instant.now().plusMillis( 7 * 24 * 60 * 60 * 1000)
        );

        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new BusinessException("Refresh token expired");
        }
        return token;
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest token) {
        String requestToken = token.refreshToken();

        return refreshTokenRepository
                .findByToken(requestToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String accessToken =
                            jwtService.generateToken(userDetails);
                    return new AuthResponse(
                                    accessToken,
                                    requestToken
                            );
                })
                .orElseThrow(() ->
                        new BusinessException("Refresh token not found")
                );
    }

}
