package org.geoandri.saas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
  @Id private UUID id;

  private String username;

  private String password;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
  private Set<Authority> authorities;

  private String firstName;

  private String lastName;

  private String email;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  public User(
      UUID id,
      String username,
      String password,
      boolean accountNonExpired,
      boolean accountNonLocked,
      boolean credentialsNonExpired,
      boolean enabled,
      Set<Authority> authorities,
      String firstName,
      String lastName,
      String email,
      Company company) {
    this.id = id;
    this.username = username;
    this.password = password;
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

  public User() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return accountNonExpired == user.accountNonExpired
        && accountNonLocked == user.accountNonLocked
        && credentialsNonExpired == user.credentialsNonExpired
        && enabled == user.enabled
        && Objects.equals(id, user.id)
        && Objects.equals(username, user.username)
        && Objects.equals(password, user.password)
        && Objects.equals(authorities, user.authorities)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(email, user.email)
        && Objects.equals(company, user.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        username,
        password,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        enabled,
        authorities,
        firstName,
        lastName,
        email,
        company);
  }
}
