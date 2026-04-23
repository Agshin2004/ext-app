package com.agshin.extapp.features.expense.infrastructure;

import com.agshin.extapp.features.expense.api.dto.CreateRecurringExpenseRequest;
import com.agshin.extapp.features.expense.api.dto.RecurringExpenseResponse;
import com.agshin.extapp.features.expense.domain.RecurringExpense;
import com.agshin.extapp.features.user.infrastructure.UserMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RecurringExpenseMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "nextRunDate", expression = "java(LocalDateTime.now())")
    RecurringExpense toEntity(CreateRecurringExpenseRequest request);

    @Mapping(target = "categoryId", expression = "java(expense.getCategory() != null ? expense.getCategory().getId() : null)")
    RecurringExpenseResponse toResponse(RecurringExpense expense);
}
