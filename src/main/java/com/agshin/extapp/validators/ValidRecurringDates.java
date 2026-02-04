package com.agshin.extapp.validators;

import jakarta.validation.Payload;

public @interface ValidRecurringDates {

    String message() default "Invalid recurring expense date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
