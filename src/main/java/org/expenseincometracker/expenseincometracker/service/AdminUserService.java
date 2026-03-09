package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface AdminUserService {
    Page<UserResponse> getAllUsers(int page,int size);
    Page<UserResponse> getAllSUSPENDEDUsers(int page,int size);
    UserResponse getUserById(Long userId);
    UserResponse changeUserStatus(Long userId);
    void deleteUser(Long userId);
}
