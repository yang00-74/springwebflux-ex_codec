package com.nathan.ex.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniqueBookValidator.class)
public @interface UniqueBook {

    String message() default "该书已存在，请重试";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
