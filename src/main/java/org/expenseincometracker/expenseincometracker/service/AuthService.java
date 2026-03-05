package org.expenseincometracker.expenseincometracker.service;


import org.expenseincometracker.expenseincometracker.dto.request.LoginRequest;
import org.expenseincometracker.expenseincometracker.dto.request.RegisterRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void registerParent(RegisterRequest request);
    void registerAdmin(RegisterRequest request);
}
