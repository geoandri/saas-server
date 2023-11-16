package org.geoandri.saas.configuration;

import org.geoandri.saas.repository.JpaRegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
public class AddClient {

  private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

  public AddClient(JpaRegisteredClientRepository jpaRegisteredClientRepository) {
    this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
  }

  //      @PostConstruct
  //      public void initPublicClient() {
  //          RegisteredClient publicClient =
  //                  RegisteredClient.withId(UUID.randomUUID().toString())
  //                          .clientId("public-client")
  //
  //                          .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
  //
  //                          .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
  //                          .redirectUri("http://127.0.0.1:4200/home")
  //                          .redirectUri("http://127.0.0.1:4200/silent-renew.html")
  //
  //                          .postLogoutRedirectUri("http://127.0.0.1:4200")
  //                          .scope(OidcScopes.OPENID)
  //                          .scope(OidcScopes.PROFILE)
  //                          .clientIdIssuedAt(Instant.now())
  //                          .clientSettings(
  //                                  ClientSettings.builder()
  //                                          .requireAuthorizationConsent(false)
  //                                          .requireProofKey(true)
  //                                          .build())
  //                          .build();
  //
  //          jpaRegisteredClientRepository.save(publicClient);
  //      }
}
