package org.cenchev.hoamanagerapp.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cenchev.hoamanagerapp.annotations.PasswordMatches;
import org.cenchev.hoamanagerapp.model.bindings.UserRegisterBindingModel;

public class PasswordMatchValidator  implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserRegisterBindingModel user = (UserRegisterBindingModel) obj;
        return user.getPassword().equals(user.getConfirmPassword());

    }
}
