package com.agshin.extapp.features.expense.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = RecurringDateValidator.class)
public @interface ValidRecurringDates {

    String message() default "Invalid recurring expense date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
