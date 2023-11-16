package org.geoandri.saas.dto.response;

import java.util.List;

public record UserPageResponseDto(
    List<UserResponseDto> users,
    long totalElements,
    int totalPages,
    int number,
    int size,
    int numberOfElements,
    boolean first,
    boolean last) {}
