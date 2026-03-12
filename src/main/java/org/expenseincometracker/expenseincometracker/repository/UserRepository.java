package org.expenseincometracker.expenseincometracker.repository;


import jakarta.validation.constraints.NotBlank;
import org.expenseincometracker.expenseincometracker.dto.response.MonthlyUserRegistrationResponse;
import org.expenseincometracker.expenseincometracker.dto.response.RecentUserResponse;
import org.expenseincometracker.expenseincometracker.dto.response.UserResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(@NotBlank String email);
    List<User> findByParentId(Long parentId);
    @Query("""
        SELECT new org.expenseincometracker.expenseincometracker.dto.response.UserResponse(
            u.id,
            u.name,
            u.email,
            u.role,
            u.status,
            COUNT(t.id),
            u.createdAt
        )
        FROM User u
        LEFT JOIN Transaction t ON t.createdBy.id = u.id
        WHERE u.role = 'ROLE_PARENT' or u.role = 'ROLE_CHILD'
        GROUP BY u.id
        """)
    Page<UserResponse> findAllUsersWithTransactionCount(Pageable pageable);

    @Query("""
        SELECT new org.expenseincometracker.expenseincometracker.dto.response.UserResponse(
            u.id,
            u.name,
            u.email,
            u.role,
            u.status,
            COUNT(t.id),
            u.createdAt
        )
        FROM User u
        LEFT JOIN Transaction t ON t.createdBy.id = u.id
        WHERE u.role IN (:roles) 
        AND u.status = :status
        GROUP BY u.id, u.name, u.email, u.role, u.status, u.createdAt
""")
    Page<UserResponse> findAllSuspendedParentsAndChildren(
            @Param("roles") List<Role> roles,
            @Param("status") UserStatus status,
            Pageable pageable
    );

    long countByRoleAndStatus(Role role, UserStatus status);

    long countByRole(Role role);

    @Query("""
        SELECT new org.expenseincometracker.expenseincometracker.dto.response.RecentUserResponse(
            u.name,
            u.email,
            u.createdAt,
            u.role
        )
        FROM User u
        WHERE u.role IN (:roles) 
        ORDER BY u.createdAt DESC
    """)
    Page<RecentUserResponse> findRecentUsers(
            @Param("roles") List<Role> roles,
            Pageable pageable
    );

    @Query("""
        SELECT new org.expenseincometracker.expenseincometracker.dto.response.MonthlyUserRegistrationResponse(
            YEAR(u.createdAt),
            MONTH(u.createdAt),
            COUNT(u.id)
        )
        FROM User u
        WHERE u.createdAt >= :startDate
        AND u.role IN (:roles) 
        GROUP BY YEAR(u.createdAt), MONTH(u.createdAt)
        ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)
    """)
    List<MonthlyUserRegistrationResponse> findMonthlyUserRegistrationsSince(
            @Param("roles") List<Role> roles,
            @Param("startDate") LocalDateTime startDate
    );

    @Query(
        value = """
            SELECT COUNT(u) 
            FROM User u 
            WHERE u.role IN (:roles)
         """,nativeQuery = true
    )
    long countParentAndChildrenUsers(
            @Param("roles") List<Role> roles
    );

}
