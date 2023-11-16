package org.geoandri.saas.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.geoandri.saas.entity.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

  public static final String DEF_USERS_BY_USERNAME_QUERY =
      "select username,password,enabled,"
          + "first_name,last_name,email,"
          + "account_non_expired, account_non_locked, credentials_non_expired "
          + "from users "
          + "where username = ?";

  public static final String DEF_COMPANY_BY_ID_QUERY =
      "select name " + "from companies " + "where id = uuid(?)";

  public static final String DEF_COMPANY_IS_BY_USERNAME_QUERY =
      "select company_id " + "from users " + "where username = ?";

  public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
      "select username,authority " + "from authorities " + "where username = ?";

  private final JdbcTemplate jdbcTemplate;

  public CustomUserDetailsService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    List<CustomUser> users = loadUsersByUsername(username);

    if (users.size() == 0) {
      logger.debug("Query returned no results for user '" + username + "'");
      throw new UsernameNotFoundException("Username" + username + "not found");
    }
    CustomUser user = users.get(0); // contains no GrantedAuthority[]
    Set<GrantedAuthority> dbAuthsSet = new HashSet<>();

    dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
    List<GrantedAuthority> dbAuths = new ArrayList<>(dbAuthsSet);

    if (dbAuths.size() == 0) {
      logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");
      throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
    }

    if (!user.isEnabled()) {
      throw new DisabledException("User " + username + " is disabled");
    }

    if (!user.isAccountNonExpired()) {
      throw new AccountExpiredException("User " + username + " is expired");
    }

    if (!user.isAccountNonLocked()) {
      throw new LockedException("User " + username + " is locked");
    }

    if (!user.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException("User " + username + " has expired credentials");
    }

    return createUserDetails(username, user, dbAuths);
  }

  protected List<CustomUser> loadUsersByUsername(String username) {
    UUID companyId = loadCompanyIdByUsername(username);
    String companyName = loadCompanyById(companyId);

    // @formatter:off
    RowMapper<CustomUser> mapper =
        (rs, rowNum) -> {
          String username1 = rs.getString(1);
          String password = rs.getString(2);
          boolean enabled = rs.getBoolean(3);
          return new CustomUser(
              username1,
              password,
              rs.getBoolean(8),
              rs.getBoolean(8),
              rs.getBoolean(9),
              enabled,
              Collections.emptySet(),
              rs.getString(4),
              rs.getString(5),
              rs.getString(6),
              companyName);
        };
    // @formatter:on
    return jdbcTemplate.query(DEF_USERS_BY_USERNAME_QUERY, mapper, username);
  }

  protected List<GrantedAuthority> loadUserAuthorities(String username) {
    return jdbcTemplate.query(
        DEF_AUTHORITIES_BY_USERNAME_QUERY,
        (rs, rowNum) -> {
          String roleName = rs.getString(2);
          return new SimpleGrantedAuthority(roleName);
        },
        username);
  }

  protected String loadCompanyById(UUID id) {
    return jdbcTemplate
        .query(DEF_COMPANY_BY_ID_QUERY, (rs, rowNum) -> rs.getString(1), id.toString())
        .get(0);
  }

  protected UUID loadCompanyIdByUsername(String username) {
    return jdbcTemplate
        .query(
            DEF_COMPANY_IS_BY_USERNAME_QUERY,
            (rs, rowNum) -> UUID.fromString(rs.getString(1)),
            username)
        .get(0);
  }

  protected UserDetails createUserDetails(
      String username, CustomUser userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
    String returnUsername = userFromUserQuery.getUsername();

    return new CustomUser(
        returnUsername,
        userFromUserQuery.getPassword(),
        userFromUserQuery.isEnabled(),
        userFromUserQuery.isAccountNonExpired(),
        userFromUserQuery.isCredentialsNonExpired(),
        userFromUserQuery.isAccountNonLocked(),
        new HashSet<>(combinedAuthorities),
        userFromUserQuery.getFirstName(),
        userFromUserQuery.getLastName(),
        userFromUserQuery.getEmail(),
        userFromUserQuery.getCompany());
  }
}
