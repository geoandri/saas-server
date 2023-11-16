package org.geoandri.saas.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.geoandri.saas.annotation.PasswordMatches;
import org.geoandri.saas.annotation.ValidPassword;

@PasswordMatches
public record PasswordResetDto(
    @ValidPassword(message = "Password is weak") String password,
    String passwordConfirmation,
    @NotNull(message = "Token cannot be blank") UUID token) {}
