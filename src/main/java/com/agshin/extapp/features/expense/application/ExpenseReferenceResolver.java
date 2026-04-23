package com.agshin.extapp.features.expense.application;

import com.agshin.extapp.features.category.domain.Category;
import com.agshin.extapp.features.category.infrastructure.CategoryRepository;
import com.agshin.extapp.features.expense.domain.RecurringExpense;
import com.agshin.extapp.features.expense.infrastructure.RecurringExpenseRepository;
import com.agshin.extapp.shared.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ExpenseReferenceResolver {
    private final CategoryRepository categoryRepository;
    private final RecurringExpenseRepository expenseRepository;

    public ExpenseReferenceResolver(CategoryRepository categoryRepository, RecurringExpenseRepository expenseRepository) {
        this.categoryRepository = categoryRepository;
        this.expenseRepository = expenseRepository;
    }

    public Category resolveCategory(Long id, Long userId) {
        return categoryRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    public RecurringExpense resolveRecurringExpense(Long id, Long userId) {
        return expenseRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new DataNotFoundException("Recurring Expense not found"));
    }
}
