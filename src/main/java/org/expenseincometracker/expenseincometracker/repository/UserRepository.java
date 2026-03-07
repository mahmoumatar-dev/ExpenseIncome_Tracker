package org.expenseincometracker.expenseincometracker.repository;


import jakarta.validation.constraints.NotBlank;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(@NotBlank String email);
    List<User> findByParentId(Long parentId);
}
