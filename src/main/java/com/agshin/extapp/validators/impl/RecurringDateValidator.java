package com.agshin.extapp.validators.impl;

import com.agshin.extapp.model.request.expense.CreateRecurringExpenseRequest;
import com.agshin.extapp.validators.ValidRecurringDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class RecurringDateValidator implements ConstraintValidator<ValidRecurringDates, CreateRecurringExpenseRequest> {
    @Override
    public void initialize(ValidRecurringDates constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateRecurringExpenseRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();

        boolean valid = true;

        if (start != null && start.isBefore(LocalDateTime.now())) {
            context.buildConstraintViolationWithTemplate("startDate must not be in the past")
                    .addPropertyNode("startDate")
                    .addConstraintViolation();
            valid = false;
        }

        if (start != null && end != null && end.isAfter(start)) {
            context.buildConstraintViolationWithTemplate("endDate must be after startDate")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();

            valid = false;
        }

        return valid;
    }
}
