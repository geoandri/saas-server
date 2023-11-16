package org.geoandri.saas.dto.response;

public record ValidationError(String field, String rejectedValue, String message) {}
