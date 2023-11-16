package org.geoandri.saas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import org.geoandri.saas.shared.JwtBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

@DisplayName("Integrations test for Dummy controller")
public class DummyControllerTests extends RestControllerTests {

  @Test
  @DisplayName("Get super admin endpoint with not token should return unauthorized")
  public void testNoToken() throws Exception {
    mockMvc.perform(get("/test-auth/super-admin")).andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Get super admin endpoint with super admin role should return success")
  public void testSuperAdminToSuperAdminEndpointSuccess() throws Exception {
    mockMvc
        .perform(
            get("/test-auth/super-admin")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "test", Set.of("SUPER_ADMIN")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isOk())
        .andExpect(content().string("test Super Admin success"));
  }

  @Test
  @DisplayName("Get user endpoint with super admin role should return success")
  public void testSuperAdminToSuperUserEndpointSuccess() throws Exception {
    mockMvc
        .perform(
            get("/test-auth/user")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "test", Set.of("SUPER_ADMIN")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isOk())
        .andExpect(content().string("test User success"));
  }

  @Test
  @DisplayName("Get super admin endpoint endpoint with user role should return forbidden")
  public void testUserToSuperUserEndpointFailure() throws Exception {
    mockMvc
        .perform(
            get("/test-auth/super-admin")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "test", Set.of("USER")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Get user endpoint endpoint with user role should return success")
  public void testUserToSuperUserEndpointSuccess() throws Exception {
    mockMvc
        .perform(
            get("/test-auth/user")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "test", Set.of("USER")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isOk())
        .andExpect(content().string("test User success"));
  }
}
