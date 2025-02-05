package in.ac.iitj.instiapp.constraints;

import in.ac.iitj.instiapp.validators.MonthValidator;
import in.ac.iitj.instiapp.validators.YearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Validations {
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = YearValidator.class)
    public @interface ValidYear {
        String message() default "Invalid year for mess menu";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = MonthValidator.class)
    public @interface ValidMonth {
        String message() default "Invalid month for mess menu";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}