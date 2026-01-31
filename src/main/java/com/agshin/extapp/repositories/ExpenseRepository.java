package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUser_Id(Long id, Pageable pageable);
    Optional<Expense> findByIdAndUser_Id(Long id, Long userId);
}
