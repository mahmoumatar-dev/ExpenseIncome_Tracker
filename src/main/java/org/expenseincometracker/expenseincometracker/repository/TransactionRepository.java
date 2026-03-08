package org.expenseincometracker.expenseincometracker.repository;

import org.expenseincometracker.expenseincometracker.dto.response.ParentTransactionResponse;
import org.expenseincometracker.expenseincometracker.entity.Transaction;
import org.expenseincometracker.expenseincometracker.dto.response.CategorySpendingResponse;
import org.expenseincometracker.expenseincometracker.dto.response.ChildSpendingResponse;
import org.expenseincometracker.expenseincometracker.dto.response.IncomeExpenseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

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


    @Query(value = """
        SELECT COALESCE(AVG(month_total), 0)
        FROM (
            SELECT SUM(t.amount) AS month_total
            FROM transactions t
            LEFT JOIN users u ON t.created_by = u.id
            WHERE t.type = 'EXPENSE'
              AND (u.id = :parentId OR u.parent_id = :parentId)
            GROUP BY DATE_TRUNC('month', t.created_at)
        ) AS monthly_sums
        """, nativeQuery = true)
    BigDecimal averageMonthlySpending(@Param("parentId") Long parentId);

    @Query(value = """
        SELECT c.name
        FROM transactions t
        JOIN categories c ON t.category_id = c.id
        JOIN users u ON t.created_by = u.id
        WHERE t.type = 'EXPENSE'
          AND (u.id = :parentId OR u.parent_id = :parentId)
        GROUP BY c.name
        ORDER BY SUM(t.amount) DESC
        LIMIT 1
        """, nativeQuery = true)
    String findTopCategory(@Param("parentId") Long parentId);


    @Query("""
       SELECT COALESCE(SUM(t.amount), 0)
       FROM Transaction t
       WHERE t.createdBy.parent.id = :parentId
       AND t.type = org.expenseincometracker.expenseincometracker.enums.TransactionType.EXPENSE
       """)
    BigDecimal sumChildrenSpendingByParent(@Param("parentId") Long parentId);


    @Query("""
            SELECT new org.expenseincometracker.expenseincometracker.dto.response.CategorySpendingResponse(
                c.name,
                SUM(t.amount)
            )
            FROM Transaction t
            JOIN t.category c
            WHERE t.type = org.expenseincometracker.expenseincometracker.enums.TransactionType.EXPENSE
              AND (t.createdBy.id = :parentId OR t.createdBy.parent.id = :parentId)
            GROUP BY c.name
            ORDER BY SUM(t.amount) DESC
        """)
    List<CategorySpendingResponse> spendingByCategory(@Param("parentId") Long parentId);


    @Query("""
            SELECT new org.expenseincometracker.expenseincometracker.dto.response.IncomeExpenseResponse(
                DATE_TRUNC('month', t.createdAt),
                COALESCE(SUM(CASE WHEN t.type = org.expenseincometracker.expenseincometracker.enums.TransactionType.INCOME THEN t.amount ELSE 0 END),0),
                COALESCE(SUM(CASE WHEN t.type = org.expenseincometracker.expenseincometracker.enums.TransactionType.EXPENSE THEN t.amount ELSE 0 END),0)
            )
            FROM Transaction t
            WHERE (t.createdBy.id = :parentId OR t.createdBy.parent.id = :parentId)
              AND t.createdAt >= :since
            GROUP BY DATE_TRUNC('month', t.createdAt)
            ORDER BY DATE_TRUNC('month', t.createdAt)
        """)
    List<IncomeExpenseResponse> incomeVsExpense(
            @Param("parentId") Long parentId,
            @Param("since") LocalDateTime since
    );

    @Query("""
        SELECT new org.expenseincometracker.expenseincometracker.dto.response.ChildSpendingResponse(
            t.createdBy.id,
            SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.type = org.expenseincometracker.expenseincometracker.enums.TransactionType.EXPENSE
          AND t.createdBy.parent.id = :parentId
          AND FUNCTION('DATE_TRUNC','month', t.createdAt) =
              FUNCTION('DATE_TRUNC','month', CURRENT_TIMESTAMP)
        GROUP BY t.createdBy.id
    """)
    List<ChildSpendingResponse> sumChildrenExpensesThisMonth(@Param("parentId") Long parentId);


    @Query("""
       SELECT new org.expenseincometracker.expenseincometracker.dto.response.ParentTransactionResponse(
           t.createdAt,
           c.name,
           w.name,
           t.type,
           t.amount
       )
       FROM Transaction t
       JOIN t.wallet w
       LEFT JOIN t.category c
       WHERE w.owner.id = :parentId
       ORDER BY t.createdAt DESC
       """)
    Page<ParentTransactionResponse> findParentTransactions(
            @Param("parentId") Long parentId,
            Pageable pageable
    );
}
