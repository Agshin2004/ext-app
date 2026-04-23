package com.agshin.extapp.features.expense.domain;

public enum RecurringExpenseFrequency {
    DAILY("1"), WEEKLY("7"), MONTHLY("30"), YEARLY("365");

    private final String day;
    RecurringExpenseFrequency(String number) {
        this.day = number;
    }
}
