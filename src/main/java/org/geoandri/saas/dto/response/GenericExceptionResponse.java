package org.geoandri.saas.dto.response;

import static org.geoandri.saas.constants.ApplicationConstants.ISO_DATE_FORMAT;
import static org.geoandri.saas.constants.ApplicationConstants.UTC_TIMEZONE;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public record GenericExceptionResponse(
    String message,
    @JsonFormat(pattern = ISO_DATE_FORMAT, timezone = UTC_TIMEZONE) Instant timestamp) {}
