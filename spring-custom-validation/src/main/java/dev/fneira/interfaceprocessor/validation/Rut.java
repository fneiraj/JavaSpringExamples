package dev.fneira.interfaceprocessor.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RutValidator.class)
public @interface Rut {
  public String message() default "must be a well-formed RUT (e.g. 12345678-9)";

  public Class<?>[] groups() default {};

  public Class<? extends Payload>[] payload() default {};
}
