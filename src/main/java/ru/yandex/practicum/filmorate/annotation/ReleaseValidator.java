package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseValidator implements ConstraintValidator<Release, LocalDate> {
    private static final LocalDate EARLIER_RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(EARLIER_RELEASE);
    }

    @Override
    public void initialize(Release constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}