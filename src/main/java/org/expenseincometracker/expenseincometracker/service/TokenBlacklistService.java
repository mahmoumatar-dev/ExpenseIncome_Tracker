package org.expenseincometracker.expenseincometracker.service;

public interface TokenBlacklistService {

    void revokeToken(String token);
    boolean isTokenRevoked(String token);
}
