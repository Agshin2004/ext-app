package com.agshin.extapp.resolvers;

import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.CategoryRepository;
import com.agshin.extapp.repositories.RecurringExpenseRepository;
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
