package in.ac.iitj.instiapp.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidationUtil {

    private final Validator validator;

    public ValidationUtil() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    // Generic validation method for any object
    public <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            throw new ConstraintViolationException(errorMessage, violations);
        }
    }

    // Specific validation methods for common types
    public String validateString(String input, int minLength, int maxLength) throws  ConstraintViolationException{


        if (input == null || input.trim().length() < minLength || input.trim().length() > maxLength) {
            throw new ConstraintViolationException("String length must be between " + minLength + " and " + maxLength, null);
        }
        return input;
    }

    public String validateEmail(String email) throws ConstraintViolationException{
        validateString(email, 12, 320);
        if(!email.contains("@")){
            throw new ConstraintViolationException("Invalid email", null);
        }
        return email;
    }

    public Integer validateInteger(Integer input, int min, int max) throws  ConstraintViolationException{
        if (input == null || input < min || input > max) {
            throw new ConstraintViolationException("Integer must be between " + min + " and " + max, null);
        }

        return  input;
    }

    public void validateDate(java.util.Date input) throws  ConstraintViolationException{
        if (input == null) {
            throw new ConstraintViolationException("Date cannot be null", null);
        }
    }
}