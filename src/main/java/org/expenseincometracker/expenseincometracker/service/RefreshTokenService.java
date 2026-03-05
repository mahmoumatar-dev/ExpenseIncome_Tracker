package org.expenseincometracker.expenseincometracker.service;


import org.expenseincometracker.expenseincometracker.dto.request.RefreshTokenRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthResponse;
import org.expenseincometracker.expenseincometracker.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String userEmail);
    RefreshToken verifyExpiration(RefreshToken token);
    AuthResponse refreshToken(RefreshTokenRequest token);

}
