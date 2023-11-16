package org.geoandri.saas.dto.request;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UploadedUserDto(
    @NotBlank(message = "First name cannot be blank") @CsvBindByPosition(position = 0)
        String firstName,
    @NotBlank(message = "Last name cannot be blank") @CsvBindByPosition(position = 1)
        String lastName,
    @Email(message = "Email field should be a valid email address") @CsvBindByPosition(position = 2)
        String email) {}
