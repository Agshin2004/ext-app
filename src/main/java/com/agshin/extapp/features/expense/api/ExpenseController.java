package com.agshin.extapp.features.expense.api;

import com.agshin.extapp.features.expense.api.dto.CreateExpenseRequest;
import com.agshin.extapp.features.expense.api.dto.CreateRecurringExpenseRequest;
import com.agshin.extapp.features.expense.api.dto.ExpenseDto;
import com.agshin.extapp.features.expense.api.dto.ExpenseResponse;
import com.agshin.extapp.features.expense.api.dto.RecurringExpenseResponse;
import com.agshin.extapp.features.expense.api.dto.UpdateExpenseRequest;
import com.agshin.extapp.features.expense.application.ExpenseService;
import com.agshin.extapp.shared.api.ApplicationConstants;
import com.agshin.extapp.shared.api.GenericResponse;
import com.agshin.extapp.shared.api.PagedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/recurring")
    public ResponseEntity<GenericResponse<RecurringExpenseResponse>> addRecurringExpense(@RequestBody CreateRecurringExpenseRequest request) {
        var recurringExpense = expenseService.createRecurringExpense(request);

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                recurringExpense,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ExpenseResponse>> getOne(@PathVariable("id") Long id) {
        ExpenseResponse expenseById = expenseService.getById(id);

        var response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                expenseById,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PagedResponse<List<ExpenseDto>>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        PagedResponse<List<ExpenseDto>> allUserExpenses = expenseService.getAllExpenses(page, size, asc);

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, allUserExpenses, HttpStatus.OK.value());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PostMapping
    public ResponseEntity<GenericResponse<ExpenseResponse>> createExpense(@RequestBody CreateExpenseRequest request) {
        var expense = expenseService.createExpense(request);

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, expense, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericResponse<ExpenseResponse>> updateExpense(
            @PathVariable("id") Long id,
            @RequestBody UpdateExpenseRequest request
    ) {
        var expense = expenseService.updateExpense(id, request);

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, expense, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteExpense(@PathVariable("id") Long id) {
        expenseService.deleteExpense(id);

        GenericResponse<Void> response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                null,
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
                .body(response);
    }

    @GetMapping("/category")
    public ResponseEntity<GenericResponse<?>> getExpensesByCategory(@RequestParam("category") String categoryName) {
        var allExpensesByCategory = expenseService.getAllExpensesByCategory(categoryName);

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, allExpensesByCategory, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }
}
