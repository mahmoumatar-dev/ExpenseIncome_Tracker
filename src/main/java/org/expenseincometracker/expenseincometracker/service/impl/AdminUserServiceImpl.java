package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.UserResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.service.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Page<UserResponse> getAllUsers(int page,int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAllUsersWithTransactionCount(pageable);
    }

    @Override
    public Page<UserResponse> getAllSUSPENDEDUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Role> roles = List.of(Role.ROLE_PARENT, Role.ROLE_CHILD);

        return userRepository.findAllSuspendedParentsAndChildren(
                roles,
                UserStatus.SUSPENDED,
                pageable
        );
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        long transactionCount = transactionRepository.countByCreatedBy_Id(userId);
        return mapToUserResponse(user, transactionCount);
    }

    @Override
    public UserResponse changeUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(
                user.getStatus()==UserStatus.SUSPENDED ?
                UserStatus.ACTIVE : UserStatus.SUSPENDED
        );
        userRepository.save(user);
        long transactionCount = transactionRepository.countByCreatedBy_Id(userId);
        return mapToUserResponse(user, transactionCount);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() == Role.ROLE_ADMIN){
            throw new RuntimeException("Admin account cannot be deleted");
        }
        userRepository.delete(user);
    }

    private UserResponse mapToUserResponse(User user, long transactionCount){

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                transactionCount,
                user.getCreatedAt()
        );
    }
}
