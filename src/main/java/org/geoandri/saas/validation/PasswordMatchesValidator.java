package org.geoandri.saas.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.geoandri.saas.annotation.PasswordMatches;
import org.geoandri.saas.dto.request.PasswordResetDto;
import org.geoandri.saas.dto.request.UserRegistrationDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    if (obj instanceof UserRegistrationDto user) {
      return user.password().equals(user.passwordConfirmation());
    } else if (obj instanceof PasswordResetDto passwordReset) {
      return passwordReset.password().equals(passwordReset.passwordConfirmation());
    } else {
      return false;
    }
  }
}
