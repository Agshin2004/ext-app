package com.agshin.extapp.controllers;

import com.agshin.extapp.ExpenseDto;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.request.expense.CreateExpenseRequest;
import com.agshin.extapp.model.dto.expense.PagedResponse;
import com.agshin.extapp.model.response.expense.ExpenseResponse;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.services.ExpenseService;
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
}
