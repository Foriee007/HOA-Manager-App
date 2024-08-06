package org.cenchev.hoamanagerapp.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.cenchev.hoamanagerapp.utils.PasswordMatchValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password does not match";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
