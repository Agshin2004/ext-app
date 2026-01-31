package com.agshin.extapp.mappers;

import com.agshin.extapp.model.dto.expense.ExpenseDto;
import com.agshin.extapp.model.entities.Expense;
import com.agshin.extapp.model.request.expense.CreateExpenseRequest;
import com.agshin.extapp.model.request.expense.UpdateExpenseRequest;
import com.agshin.extapp.model.response.expense.ExpenseResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExpenseMapper {
    // map only scalars as mapper should
    @Mapping(target = "expenseDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    Expense toEntity(CreateExpenseRequest request);

    @Mapping(
            target = "categoryId",
            expression = "java(expense.getCategory() != null ? expense.getCategory().getId() : null)"
    )
    @Mapping(target = "userId", expression = "java(expense.getUser().getId())")
    ExpenseResponse toResponse(Expense expense);

    @Mapping(
            target = "categoryId",
            expression = "java(expense.getCategory() != null ? expense.getCategory().getId() : null)"
    )
    @Mapping(
            target = "userId",
            expression = "java(expense.getUser() != null ? expense.getUser().getId() : null)"
    )
    @Mapping(
            target = "recurringExpenseId",
            expression = "java(expense.getRecurringExpense() != null ? expense.getRecurringExpense().getId() : null)"
    )
    ExpenseDto toDto(Expense expense);

    void updateExpenseFromRequest(UpdateExpenseRequest request, @MappingTarget Expense expense);
}
