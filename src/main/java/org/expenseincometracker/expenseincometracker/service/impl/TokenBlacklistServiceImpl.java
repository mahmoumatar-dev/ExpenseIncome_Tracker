package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.entity.RevokedToken;
import org.expenseincometracker.expenseincometracker.repository.RevokedTokenRepository;
import org.expenseincometracker.expenseincometracker.service.TokenBlacklistService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RevokedTokenRepository revokedTokenRepository;

    @Override
    public void revokeToken(String token) {
        RevokedToken revokedToken = new RevokedToken();

        revokedToken.setToken(token);
        revokedToken.setRevokedAt(LocalDateTime.now());

        revokedTokenRepository.save(revokedToken);
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return revokedTokenRepository.existsByToken(token);
    }
}
