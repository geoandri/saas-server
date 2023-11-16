package org.geoandri.saas.entity;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUser implements UserDetails {

  private String password;

  private String username;

  public CustomUser() {}

  private Set<GrantedAuthority> authorities;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  private String firstName;

  private String lastName;

  private String email;

  private String company;

  public CustomUser(
      String username,
      String password,
      boolean accountNonExpired,
      boolean accountNonLocked,
      boolean credentialsNonExpired,
      boolean enabled,
      Set<GrantedAuthority> authorities,
      String firstName,
      String lastName,
      String email,
      String company) {
    this.password = password;
    this.username = username;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
    this.authorities = authorities;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.company = company;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getCompany() {
    return company;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomUser that = (CustomUser) o;
    return accountNonExpired == that.accountNonExpired
        && accountNonLocked == that.accountNonLocked
        && credentialsNonExpired == that.credentialsNonExpired
        && enabled == that.enabled
        && Objects.equals(password, that.password)
        && Objects.equals(username, that.username)
        && Objects.equals(authorities, that.authorities)
        && Objects.equals(firstName, that.firstName)
        && Objects.equals(lastName, that.lastName)
        && Objects.equals(email, that.email)
        && Objects.equals(company, that.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        password,
        username,
        authorities,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        enabled,
        firstName,
        lastName,
        email,
        company);
  }
}
