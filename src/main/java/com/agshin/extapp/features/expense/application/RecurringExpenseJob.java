package com.agshin.extapp.features.expense.application;

import com.agshin.extapp.features.audit.domain.AuditType;
import com.agshin.extapp.features.audit.infrastructure.annotations.Auditable;
import com.agshin.extapp.features.expense.domain.Currency;
import com.agshin.extapp.features.expense.domain.Expense;
import com.agshin.extapp.features.expense.domain.RecurringExpense;
import com.agshin.extapp.features.expense.domain.RecurringExpenseFrequency;
import com.agshin.extapp.features.expense.infrastructure.ExpenseRepository;
import com.agshin.extapp.features.expense.infrastructure.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurringExpenseJob {
    private final RecurringExpenseRepository recurringExpenseRepo;
    private final ExpenseRepository expenseRepo;

    public RecurringExpenseJob(RecurringExpenseRepository recurringExpenseRepo, ExpenseRepository expenseRepo) {
        this.recurringExpenseRepo = recurringExpenseRepo;
        this.expenseRepo = expenseRepo;
    }

    public List<RecurringExpense> getDueNow() {
        return recurringExpenseRepo.findActiveByFrequencyAndDate(RecurringExpenseFrequency.DAILY, LocalDateTime.now());
    }

    public void processAll() {
        var due = getDueNow();
        due.forEach(this::process);

        System.out.println("processed");
    }

    @Auditable(action = AuditType.STATE_CHANGE, entity = "RecurringExpense")
    private void process(RecurringExpense recurringExpense) {
        Expense expense = new Expense();
        expense.setUser(recurringExpense.getUser());
        expense.setCategory(recurringExpense.getCategory());
        expense.setAmount(recurringExpense.getAmount());
        expense.setDescription(recurringExpense.getDescription());
        expense.setRecurringExpense(recurringExpense);
        expense.setCurrency(Currency.AZN); // static for now
        expense.setExpenseDate(LocalDateTime.now());

        recurringExpense.setNextRunDate(
                advance(recurringExpense.getNextRunDate(), recurringExpense.getFrequency())
        );

        expenseRepo.save(expense);
        recurringExpenseRepo.save(recurringExpense);
    }

    private LocalDateTime advance(LocalDateTime nextRunDate, RecurringExpenseFrequency frequency) {
        return switch (frequency) {
            case DAILY -> nextRunDate.plusDays(1);
            case WEEKLY -> nextRunDate.plusWeeks(1);
            case MONTHLY -> nextRunDate.plusMonths(1);
            case YEARLY -> nextRunDate.plusYears(1);
        };
    }
}
