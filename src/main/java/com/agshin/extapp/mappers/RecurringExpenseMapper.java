package com.agshin.extapp.mappers;

import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.model.request.expense.CreateRecurringExpenseRequest;
import com.agshin.extapp.model.request.expense.RecurringExpenseResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RecurringExpenseMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    RecurringExpense toEntity(CreateRecurringExpenseRequest request);

    @Mapping(target = "categoryId", expression = "java(expense.getCategory() != null ? expense.getCategory().getId() : null)")
    RecurringExpenseResponse toResponse(RecurringExpense expense);
}
