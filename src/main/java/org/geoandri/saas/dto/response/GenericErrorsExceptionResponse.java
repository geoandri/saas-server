package org.geoandri.saas.dto.response;

import static org.geoandri.saas.constants.ApplicationConstants.ISO_DATE_FORMAT;
import static org.geoandri.saas.constants.ApplicationConstants.UTC_TIMEZONE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public record GenericErrorsExceptionResponse(
    @JsonProperty("errors") List<ValidationError> errors,
    @JsonFormat(pattern = ISO_DATE_FORMAT, timezone = UTC_TIMEZONE) Instant timestamp) {}
