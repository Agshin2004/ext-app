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

    public BillingScheduler(RecurringExpenseJob schedulerService) {
        this.schedulerService = schedulerService;
    }


    // TODO: implement custom retries logic
    @Scheduled(cron = "0 0 0 * * *")
    public void processDueExpenses() {
        schedulerService.processAll();
    }
}
