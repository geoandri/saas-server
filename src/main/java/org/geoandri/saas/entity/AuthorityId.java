package org.geoandri.saas.entity;

import java.io.Serializable;
import java.util.Objects;

public class AuthorityId implements Serializable {

  private String username;

  private UserRole userRole;

  public AuthorityId(String username, UserRole userRole) {
    this.username = username;
    this.userRole = userRole;
  }

  public AuthorityId() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthorityId that = (AuthorityId) o;
    return Objects.equals(username, that.username) && userRole == that.userRole;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, userRole);
  }
}
