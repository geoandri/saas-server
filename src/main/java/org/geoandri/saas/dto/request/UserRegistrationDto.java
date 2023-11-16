package org.geoandri.saas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.geoandri.saas.annotation.PasswordMatches;
import org.geoandri.saas.annotation.ValidPassword;

@PasswordMatches
public record UserRegistrationDto(
    @ValidPassword(message = "Password is weak") String password,
    String passwordConfirmation,
    @NotBlank(message = "First name cannot be blank") String firstName,
    @NotBlank(message = "Last name cannot be blank") String lastName,
    @Email(message = "Email field should be a valid email address") String email,
    @NotBlank(message = "Company cannot be blank") String company) {}
