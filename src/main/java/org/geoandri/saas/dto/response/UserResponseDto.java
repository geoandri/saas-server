package org.geoandri.saas.dto.response;

import java.util.Set;
import org.geoandri.saas.entity.UserRole;

public record UserResponseDto(
    String username,
    String firstName,
    String lasttName,
    String email,
    String company,
    Set<UserRole> roles) {}
