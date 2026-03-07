package org.expenseincometracker.expenseincometracker.repository;

import org.expenseincometracker.expenseincometracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("""
       SELECT COALESCE(SUM(t.amount),0)
       FROM Transaction t
       WHERE t.category.id = :categoryId
       AND t.createdBy.id = :parentId
       """)
    BigDecimal sumSpentByCategory(Long categoryId, Long parentId);

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.category.id = :categoryId
        AND t.type = 'EXPENSE'
        AND t.createdBy.id = :parentId
        AND DATE_TRUNC('month', t.createdAt) = DATE_TRUNC('month', CURRENT_DATE)
        """)
    BigDecimal sumCategoryExpensesThisMonth(Long categoryId, Long parentId);

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.createdBy.id = :childId
        AND t.type = 'EXPENSE'
        AND DATE_TRUNC('month', t.createdAt) = DATE_TRUNC('month', CURRENT_DATE)
        """)
    BigDecimal sumChildExpensesThisMonth(Long childId);
}
