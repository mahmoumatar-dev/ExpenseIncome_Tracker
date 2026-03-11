package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.UpdateProfileRequest;
import org.expenseincometracker.expenseincometracker.dto.response.AuthenticatedUserResponse;
import org.springframework.security.core.Authentication;

public interface ParentProfileService {
    AuthenticatedUserResponse updateProfile(Authentication authentication, UpdateProfileRequest request);
}
