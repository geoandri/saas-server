package org.geoandri.saas.dto.request;

import jakarta.validation.constraints.Email;

public record PasswordResetInitiationDto(
    @Email(message = "Email field should be a valid email address") String email) {}
