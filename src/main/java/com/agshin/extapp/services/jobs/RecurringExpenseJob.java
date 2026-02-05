package com.agshin.extapp.services.jobs;

import com.agshin.extapp.model.entities.RecurringExpense;
import com.agshin.extapp.model.enums.RecurringExpenseFrequency;
import com.agshin.extapp.repositories.RecurringExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurringExpenseJob {
    private final RecurringExpenseRepository recurringExpenseRepo;

    public RecurringExpenseJob(RecurringExpenseRepository recurringExpenseRepo) {
        this.recurringExpenseRepo = recurringExpenseRepo;
    }

    @Transactional
    public List<RecurringExpense> getDueToday() {
        LocalDateTime now = LocalDateTime.now();

        List<RecurringExpense> all = recurringExpenseRepo.findActiveByFrequencyAndDate(
                RecurringExpenseFrequency.DAILY, now
        ).orElseThrow(() -> new IllegalStateException("server error")); // todo

        all.addAll(getWeekly(LocalDateTime.now()));
        all.addAll(getMonthly(LocalDateTime.now()));
        all.addAll(getYearly(LocalDateTime.now()));

        return all;
    }

    private List<RecurringExpense> getWeekly(LocalDateTime today) {
        List<RecurringExpense> weekly = recurringExpenseRepo
                .findActiveByFrequencyAndDate(RecurringExpenseFrequency.WEEKLY, today)
                .orElseThrow(() -> new IllegalStateException("server error")); // todo

        return weekly.stream()
                .filter(r -> r.getStartDate().getDayOfWeek() == today.getDayOfWeek())
                .toList();
    }

    private List<RecurringExpense> getMonthly(LocalDateTime today) {
        List<RecurringExpense> monthly = recurringExpenseRepo
                .findActiveByFrequencyAndDate(RecurringExpenseFrequency.MONTHLY, today)
                .orElseThrow(() -> new IllegalStateException("server error")); // todo

        return monthly.stream()
                .filter(r -> r.getStartDate().getDayOfMonth() == today.getDayOfMonth())
                .toList();
    }

    private List<RecurringExpense> getYearly(LocalDateTime today) {
        List<RecurringExpense> yearly = recurringExpenseRepo
                .findActiveByFrequencyAndDate(RecurringExpenseFrequency.YEARLY, today)
                .orElseThrow(() -> new IllegalStateException("server error")); // todo

        return yearly.stream()
                .filter(r ->
                        r.getStartDate().getDayOfMonth() == today.getDayOfMonth() && r.getStartDate().getMonth() == today.getMonth()
                )
                .toList();
    }

}
