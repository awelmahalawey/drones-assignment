package utils;

import com.musala.soft.drones.exception.RequiredDataValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil {
    public static <T> void validateRequiredData(T data) throws RequiredDataValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations =
                validator.validate(data);
        if (!violations.isEmpty()) {
            List<String> errorsList = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                errorsList.add(violation.getPropertyPath().toString() + " " + violation.getMessage());
            }
            throw new RequiredDataValidationException(errorsList);
        }
    }
}
