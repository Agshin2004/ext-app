package com.agshin.extapp.features.expense.infrastructure;

import com.agshin.extapp.features.expense.domain.RecurringExpense;
import com.agshin.extapp.features.expense.domain.RecurringExpenseFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> {
    Optional<RecurringExpense> findByIdAndUser_Id(Long id, Long id1);

    Optional<RecurringExpense> findByUser_Id(Long id);


    @Query("""
                    SELECT r from RecurringExpense r
                    WHERE r.active = true
                    AND r.nextRunDate <= :today
                    AND (r.endDate IS NULL OR r.endDate >= :today)
            """)
    List<RecurringExpense> findActiveByFrequencyAndDate(
            RecurringExpenseFrequency frequency,
            LocalDateTime today
    );
}
