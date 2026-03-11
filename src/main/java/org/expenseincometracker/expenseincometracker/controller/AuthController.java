package org.expenseincometracker.expenseincometracker.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.LoginRequest;
import org.expenseincometracker.expenseincometracker.dto.request.RefreshTokenRequest;
import org.expenseincometracker.expenseincometracker.dto.request.RegisterRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.service.AuthService;
import org.expenseincometracker.expenseincometracker.service.RefreshTokenService;
import org.expenseincometracker.expenseincometracker.service.TokenBlacklistService;
import org.expenseincometracker.expenseincometracker.util.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserHelper userHelper;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest request
    ) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        authService.registerParent(request);
        return ResponseEntity.ok("Parent registered successfully");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> adminRegister(
            @RequestBody @Valid RegisterRequest request
    ) {
        authService.registerAdmin(request);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse authResponse = refreshTokenService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.revokeToken(token);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getCurrentUser(
            Authentication authentication
    ) {
       User user = userHelper.getAuthenticatedUser(authentication);
        return ResponseEntity.ok(
                ApiResponse.success(authService.getCurrentUser(user)
                )
        );
    }

}
