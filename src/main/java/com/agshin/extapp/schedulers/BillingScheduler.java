package com.agshin.extapp.schedulers;

import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.services.jobs.RecurringExpenseJob;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BillingScheduler {
    private final RecurringExpenseJob schedulerService;

    public BillingScheduler(RecurringExpenseJob schedulerService) {
        this.schedulerService = schedulerService;
    }


    // TODO: implement custom retries logic
    @Transactional
    @Scheduled(cron = "* * * * * *")
    public void processDueExpenses() {
        // todo: process data w/ orchestrator
        List<RecurringExpense> dueToday = schedulerService.getDueToday();

        dueToday.forEach(re -> System.out.println(re.toString()));

        System.out.println("processed");
    }
}
