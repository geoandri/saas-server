package org.geoandri.saas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Entity
@Table(name = "password_reset_verification_tokens")
public class PasswordResetVerificationToken {
  @Id
  @Column(name = "token", nullable = false)
  private UUID token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @Column(name = "created_at")
  @Generated(event = EventType.INSERT)
  private Instant createdAt;

  public boolean isTokenExpired() {
    return Instant.now().isAfter(createdAt.plusSeconds(86400));
  }

  public PasswordResetVerificationToken(UUID token, User user) {
    this.token = token;
    this.user = user;
  }

  public PasswordResetVerificationToken() {}

  public UUID getToken() {
    return token;
  }

  public void setToken(UUID token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PasswordResetVerificationToken that = (PasswordResetVerificationToken) o;
    return Objects.equals(token, that.token)
        && Objects.equals(user, that.user)
        && Objects.equals(createdAt, that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, user, createdAt);
  }
}
