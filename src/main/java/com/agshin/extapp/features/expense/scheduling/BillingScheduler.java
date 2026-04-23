package com.agshin.extapp.features.expense.scheduling;

import com.agshin.extapp.features.expense.application.RecurringExpenseJob;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


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
