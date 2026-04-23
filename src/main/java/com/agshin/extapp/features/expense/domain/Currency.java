package com.agshin.extapp.features.expense.domain;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    AZN("AZN"),
    BTC("BTC"),
    TON("TON");

    private final String currency;

    Currency(String curr) {
        this.currency = curr;
    }

    public static Currency fromName(String curr) {
        return Currency.fromName(curr);
    }


}
