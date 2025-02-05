package in.ac.iitj.instiapp.validators;

import in.ac.iitj.instiapp.constraints.Validations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<Validations.ValidYear, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        return year != null && year > 2007 && year <= 3000;
    }
}

