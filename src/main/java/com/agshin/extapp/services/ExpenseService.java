package com.agshin.extapp.services;

import com.agshin.extapp.model.dto.expense.ExpenseDto;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.mappers.ExpenseMapper;
import com.agshin.extapp.model.dto.expense.PagedResponse;
import com.agshin.extapp.model.entities.Category;
import com.agshin.extapp.model.entities.Expense;
import com.agshin.extapp.model.request.expense.CreateExpenseRequest;
import com.agshin.extapp.model.request.expense.UpdateExpenseRequest;
import com.agshin.extapp.model.response.expense.ExpenseResponse;
import com.agshin.extapp.repositories.ExpenseRepository;
import com.agshin.extapp.resolvers.ExpenseReferenceResolver;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ExpenseService {
    private static Logger log = LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final AuthService authService;
    private final ExpenseReferenceResolver expenseReferenceResolver;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper, AuthService authService, CategoryService categoryService, ExpenseReferenceResolver expenseReferenceResolver) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.authService = authService;
        this.expenseReferenceResolver = expenseReferenceResolver;
    }


    public ExpenseResponse getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Expense not found"));

        return expenseMapper.toResponse(expense);
    }

    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        // calling first category so resolver can check if category is owned by user
        Category category = expenseReferenceResolver.resolveCategory(
                request.categoryId(),
                authService.getCurrentUserId()
        );

        var expense = expenseMapper.toEntity(request);

        expense.setUser(authService.getCurrentUser());
        expense.setCategory(category);
        if (Objects.nonNull(request.recurringExpenseId())) {
            var recurringExpense = expenseReferenceResolver.resolveRecurringExpense(
                    request.recurringExpenseId(),
                    authService.getCurrentUserId()
            );
            expense.setRecurringExpense(recurringExpense);
        }

        expense.setExpenseDate(LocalDateTime.now());

        expenseRepository.save(expense);

        return expenseMapper.toResponse(expense);
    }

    public PagedResponse<List<ExpenseDto>> getAllExpenses(int page, int size, boolean asc) {
        Sort sort = asc
                ? Sort.by("id").ascending()
                : Sort.by("id").descending();

        PageRequest pageable = PageRequest.of(page, size ,sort);

        Page<Expense> byUserId = expenseRepository.findByUser_Id(
                authService.getCurrentUserId(),
                pageable
        );

        List<ExpenseDto> list = byUserId.getContent().stream()
                .map(expenseMapper::toDto)
                .toList();

        return new PagedResponse<List<ExpenseDto>>(list, byUserId.getNumber(), byUserId.getTotalElements(), byUserId.getTotalPages());
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {
        var currentUserId = authService.getCurrentUserId();
        var expense = expenseRepository.findByIdAndUser_Id(id, currentUserId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        expenseMapper.updateExpenseFromRequest(request, expense);

        // category can be changed
        if (Objects.nonNull(request.categoryId())) {
            var category = expenseReferenceResolver.resolveCategory(request.categoryId(), currentUserId);
            expense.setCategory(category);
        }

        expenseRepository.save(expense);

        return expenseMapper.toResponse(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        var currentUserId = authService.getCurrentUserId();
        var expense = expenseRepository.findByIdAndUser_Id(id, currentUserId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        expenseRepository.delete(expense);
    }
}
