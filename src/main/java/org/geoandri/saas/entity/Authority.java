package org.geoandri.saas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "authorities")
@IdClass(AuthorityId.class)
public class Authority {

  @Id private String username;

  @Id
  @Enumerated(EnumType.STRING)
  @Column(name = "authority")
  private UserRole userRole;

  @MapsId("username")
  @ManyToOne
  @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
  private User user;

  public Authority() {}

  public Authority(String username, UserRole authority) {
    this.username = username;
    this.userRole = authority;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Authority authority1 = (Authority) o;
    return Objects.equals(username, authority1.username) && userRole == authority1.userRole;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, userRole);
  }
}
