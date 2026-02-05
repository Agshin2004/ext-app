package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.model.enums.RecurringExpenseFrequency;
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
                    AND r.frequency = :frequency
                    AND r.startDate <= :today
                    AND (r.endDate IS NULL OR r.endDate >= :today)
            """)
    Optional<List<RecurringExpense>> findActiveByFrequencyAndDate(
            RecurringExpenseFrequency frequency,
            LocalDateTime today
    );
}
