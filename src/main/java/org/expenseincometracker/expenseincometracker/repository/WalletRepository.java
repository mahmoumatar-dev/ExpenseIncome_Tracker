package org.expenseincometracker.expenseincometracker.repository;

import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findAllByOwnerId(Long ownerId);
    Optional<Wallet> findByIdAndOwnerId(Long id, Long ownerId);
}
