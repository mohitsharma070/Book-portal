package com.example.bookportal.validation;

import com.example.bookportal.dto.RegisterForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof RegisterForm) {
            RegisterForm form = (RegisterForm) obj;
            if (form.getPassword() == null || form.getConfirmPassword() == null) return false;
            return form.getPassword().equals(form.getConfirmPassword());
        }
        return true;
    }
}
