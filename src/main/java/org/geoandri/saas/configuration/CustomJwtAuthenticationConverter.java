package org.geoandri.saas.configuration;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomJwtAuthenticationConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {
  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    List<Map<String, String>> roles = source.getClaim("authorities");

    return roles.stream()
        .map(role -> role.get("role"))
        .map(SimpleGrantedAuthority::new)
        .collect(toSet());
  }
}
