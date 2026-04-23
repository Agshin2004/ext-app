package com.agshin.extapp.features.expense.infrastructure;

import com.agshin.extapp.features.expense.api.dto.CreateExpenseRequest;
import com.agshin.extapp.features.expense.api.dto.ExpenseDto;
import com.agshin.extapp.features.expense.api.dto.ExpenseResponse;
import com.agshin.extapp.features.expense.api.dto.UpdateExpenseRequest;
import com.agshin.extapp.features.expense.domain.Expense;
import com.agshin.extapp.features.user.infrastructure.UserMapper;
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
    @Mapping(target = "isRecurring", source = "recurring")
    ExpenseDto toDto(Expense expense);

    void updateExpenseFromRequest(UpdateExpenseRequest request, @MappingTarget Expense expense);
}
