package org.geoandri.saas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "`client`")
public class Client {
  @Id private String id;

  @Column(name = "clientid")
  private String clientId;

  @Column(name = "clientidissuedat")
  private Instant clientIdIssuedAt;

  @Column(name = "clientsecret")
  private String clientSecret;

  @Column(name = "clientsecretexpiresat")
  private Instant clientSecretExpiresAt;

  @Column(name = "clientname")
  private String clientName;

  @Column(name = "clientauthenticationmethods", length = 1000)
  private String clientAuthenticationMethods;

  @Column(name = "authorizationgranttypes", length = 1000)
  private String authorizationGrantTypes;

  @Column(name = "redirecturis", length = 1000)
  private String redirectUris;

  @Column(name = "postlogoutredirecturis", length = 1000)
  private String postLogoutRedirectUris;

  @Column(length = 1000)
  private String scopes;

  @Column(name = "clientsettings", length = 2000)
  private String clientSettings;

  @Column(name = "tokensettings", length = 2000)
  private String tokenSettings;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public Instant getClientIdIssuedAt() {
    return clientIdIssuedAt;
  }

  public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
    this.clientIdIssuedAt = clientIdIssuedAt;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public Instant getClientSecretExpiresAt() {
    return clientSecretExpiresAt;
  }

  public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
    this.clientSecretExpiresAt = clientSecretExpiresAt;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientAuthenticationMethods() {
    return clientAuthenticationMethods;
  }

  public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
    this.clientAuthenticationMethods = clientAuthenticationMethods;
  }

  public String getAuthorizationGrantTypes() {
    return authorizationGrantTypes;
  }

  public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
    this.authorizationGrantTypes = authorizationGrantTypes;
  }

  public String getRedirectUris() {
    return redirectUris;
  }

  public void setRedirectUris(String redirectUris) {
    this.redirectUris = redirectUris;
  }

  public String getPostLogoutRedirectUris() {
    return this.postLogoutRedirectUris;
  }

  public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
    this.postLogoutRedirectUris = postLogoutRedirectUris;
  }

  public String getScopes() {
    return scopes;
  }

  public void setScopes(String scopes) {
    this.scopes = scopes;
  }

  public String getClientSettings() {
    return clientSettings;
  }

  public void setClientSettings(String clientSettings) {
    this.clientSettings = clientSettings;
  }

  public String getTokenSettings() {
    return tokenSettings;
  }

  public void setTokenSettings(String tokenSettings) {
    this.tokenSettings = tokenSettings;
  }
}
