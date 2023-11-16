package org.geoandri.saas.mapper;

import java.util.stream.Collectors;
import org.geoandri.saas.dto.response.UserPageResponseDto;
import org.geoandri.saas.dto.response.UserResponseDto;
import org.geoandri.saas.entity.User;
import org.springframework.data.domain.Page;

public class UserPageResponseDtoMapper {

  public static UserPageResponseDto mapToUserPageResponseDto(Page<User> userPage) {
    return new UserPageResponseDto(
        userPage.getContent().stream()
            .map(
                user ->
                    new UserResponseDto(
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getCompany().getName(),
                        user.getAuthorities().stream()
                            .map(authority -> authority.getUserRole())
                            .collect(Collectors.toSet())))
            .collect(Collectors.toList()),
        userPage.getTotalElements(),
        userPage.getTotalPages(),
        userPage.getNumber(),
        userPage.getSize(),
        userPage.getNumberOfElements(),
        userPage.isFirst(),
        userPage.isLast());
  }
}
