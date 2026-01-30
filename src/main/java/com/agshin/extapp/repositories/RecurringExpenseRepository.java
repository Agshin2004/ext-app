package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> {
    Optional<RecurringExpense> findByIdAndUser_Id(Long id, Long id1);
}
