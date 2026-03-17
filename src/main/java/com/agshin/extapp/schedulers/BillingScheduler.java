package com.agshin.extapp.schedulers;

import com.agshin.extapp.model.entities.Expense;
import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.model.enums.Currency;
import com.agshin.extapp.model.enums.RecurringExpenseFrequency;
import com.agshin.extapp.repositories.ExpenseRepository;
import com.agshin.extapp.repositories.RecurringExpenseRepository;
import com.agshin.extapp.services.jobs.RecurringExpenseJob;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class BillingScheduler {
    private final RecurringExpenseJob schedulerService;
    private final ExpenseRepository expenseRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;

    public BillingScheduler(RecurringExpenseJob schedulerService, ExpenseRepository expenseRepository, RecurringExpenseRepository recurringExpenseRepository) {
        this.schedulerService = schedulerService;
        this.expenseRepository = expenseRepository;
        this.recurringExpenseRepository = recurringExpenseRepository;
    }


    // TODO: implement custom retries logic
    @Scheduled(cron = "* 1 * * * *")
    public void processDueExpenses() {
        // todo: process data w/ orchestrator
        List<RecurringExpense> dueToday = schedulerService.getDueNow();
        System.out.println(dueToday);

        dueToday.forEach(this::process);

        System.out.println("processed");
    }

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

        expenseRepository.save(expense);
        recurringExpenseRepository.save(recurringExpense);
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
