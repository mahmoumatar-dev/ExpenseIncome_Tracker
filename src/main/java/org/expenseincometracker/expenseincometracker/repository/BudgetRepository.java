package org.expenseincometracker.expenseincometracker.repository;

import org.expenseincometracker.expenseincometracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("""
    SELECT b
    FROM Budget b
    JOIN FETCH b.category
    WHERE b.parent.id = :parentId
    """)
    List<Budget> findByParentId(Long parentId);
    Optional<Budget> findByCategoryIdAndParentId(Long categoryId, Long parentId);
}
