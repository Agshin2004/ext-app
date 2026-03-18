package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUser_Id(Long id, Pageable pageable);

    Optional<Expense> findByIdAndUser_Id(Long id, Long userId);

    @Query("""
                    SELECT e
                    FROM Expense e
                    WHERE e.user.id = :userId
                                AND e.expenseDate BETWEEN :start AND :end
            """)
    Page<Expense> findMonthlyExpenses(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("""
                SELECT SUM(e.amount) FROM Expense e
                WHERE e.user.id = :userId
                AND e.expenseDate BETWEEN :start and :end
            """)
    BigDecimal getMonthlyTotal(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
