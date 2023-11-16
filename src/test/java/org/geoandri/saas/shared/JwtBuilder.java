package org.geoandri.saas.shared;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.jwt.JoseHeaderNames;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtBuilder {

  public static Jwt jwt(String username, String company, Set<String> roles) {
    List<Map<String, String>> authoritiesMap = new ArrayList<>();
    roles.forEach(role -> authoritiesMap.add(Map.of("role", role)));

    return Jwt.withTokenValue("token-values")
        .header(JoseHeaderNames.KID, "fb14b8a688e881c9724319d9ac8fc0c7")
        .header(JoseHeaderNames.ALG, "RS256")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plus(Duration.ofDays(1)))
        .subject(username)
        .claim("company", company)
        .claim("authorities", authoritiesMap)
        .issuer("https://www.geoandri.org")
        .build();
  }
}
