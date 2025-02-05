package in.ac.iitj.instiapp.validators;

import in.ac.iitj.instiapp.constraints.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MonthValidator implements ConstraintValidator<Validations.ValidMonth, Integer> {
    @Override
    public boolean isValid(Integer month, ConstraintValidatorContext context) {
        return month != null && month >= 1 && month <= 12;
    }
}
