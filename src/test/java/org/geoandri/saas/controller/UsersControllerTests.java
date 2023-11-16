package org.geoandri.saas.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.geoandri.saas.entity.UserRole.ADMIN;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.geoandri.saas.dto.request.PasswordResetDto;
import org.geoandri.saas.dto.request.PasswordResetInitiationDto;
import org.geoandri.saas.dto.request.UserRegistrationDto;
import org.geoandri.saas.dto.response.UserPageResponseDto;
import org.geoandri.saas.entity.PasswordResetVerificationToken;
import org.geoandri.saas.entity.User;
import org.geoandri.saas.repository.PasswordResetVerificationTokenRepository;
import org.geoandri.saas.repository.RegistrationVerificationTokenRepository;
import org.geoandri.saas.repository.UsersRepository;
import org.geoandri.saas.service.NotificationService;
import org.geoandri.saas.shared.JwtBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

@DisplayName("Integrations test for Users controller")
public class UsersControllerTests extends RestControllerTests {

  @Autowired ObjectMapper objectMapper;

  @Autowired UsersRepository usersRepository;

  @Autowired RegistrationVerificationTokenRepository registrationVerificationTokenRepository;

  @Autowired PasswordResetVerificationTokenRepository passwordResetVerificationTokenRepository;

  @MockBean NotificationService notificationService;

  @MockBean PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Get users endpoint with non admin role should return forbidden")
  public void testUserToUsersEndpointFailure() throws Exception {
    mockMvc
        .perform(
            get("/users")
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "myCompany", Set.of("SUPER_ADMIN")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Get users endpoint endpoint should return only company users")
  public void testAdminToUsersEndpointSuccess() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                get("/users")
                    .with(
                        SecurityMockMvcRequestPostProcessors.jwt()
                            .jwt(JwtBuilder.jwt("test", "myCompany", Set.of("ADMIN")))
                            .authorities(customJwtAuthenticationConverter)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String response = result.getResponse().getContentAsString();
    UserPageResponseDto usersPage = objectMapper.readValue(response, UserPageResponseDto.class);

    assertThat(usersPage.totalElements()).isEqualTo(2);
    assertThat(usersPage.users().size()).isEqualTo(2);
    assertThat(usersPage.users().get(0).company()).isEqualTo("myCompany");
  }

  @Test
  @DisplayName("Register and verify user endpoints should create user and verify it")
  public void testRegisterAndVerificationSuccess() throws Exception {
    when(passwordEncoder.encode("testPassword1@")).thenReturn("encodedPassword");

    UserRegistrationDto userRegistrationDto =
        new UserRegistrationDto(
            "testPassword1@",
            "testPassword1@",
            "firstName",
            "lastName",
            "email@example.com",
            "Dummy Inc");

    mockMvc
        .perform(
            post("/users/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRegistrationDto)))
        .andDo(print())
        .andExpect(status().isOk());

    User registeredUser = usersRepository.findByUsername(userRegistrationDto.email()).get();

    assertThat(registeredUser).isNotNull();
    assertThat(registeredUser.getFirstName()).isEqualTo(userRegistrationDto.firstName());
    assertThat(registeredUser.getLastName()).isEqualTo(userRegistrationDto.lastName());
    assertThat(registeredUser.getEmail()).isEqualTo(userRegistrationDto.email());
    assertThat(registeredUser.getCompany().getName()).isEqualTo(userRegistrationDto.company());
    assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
    assertThat(registeredUser.getUsername()).isEqualTo(userRegistrationDto.email());
    assertThat(registeredUser.isEnabled()).isFalse();
    assertThat(registeredUser.isAccountNonExpired()).isTrue();
    assertThat(registeredUser.isAccountNonLocked()).isTrue();
    assertThat(registeredUser.isCredentialsNonExpired()).isTrue();
    assertThat(registeredUser.getAuthorities().size()).isEqualTo(1);
    assertThat(registeredUser.getAuthorities().stream().findFirst().get().getUserRole())
        .isEqualTo(ADMIN);

    assertThat(registrationVerificationTokenRepository.findByUser(registeredUser).isPresent())
        .isTrue();

    verify(notificationService, times(1))
        .sendRegistrationVerificationEmail(
            userRegistrationDto.email(),
            registrationVerificationTokenRepository.findByUser(registeredUser).get().getToken());

    UUID token =
        registrationVerificationTokenRepository.findByUser(registeredUser).get().getToken();

    mockMvc.perform(get("/users/verify?token=" + token)).andExpect(status().isOk()).andReturn();

    assertThat(usersRepository.findByUsername(userRegistrationDto.email()).get().isEnabled())
        .isTrue();
  }

  @Test
  @DisplayName("Reset password and verify reset endpoints should reset the password")
  public void testResetPasswordAndVerificationSuccess() throws Exception {
    when(passwordEncoder.encode("newPassword1@")).thenReturn("encodedNewPassword");

    PasswordResetInitiationDto passwordResetInitiationDto =
        new PasswordResetInitiationDto("admin@myCompany.net");

    mockMvc
        .perform(
            post("/users/password/reset")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(passwordResetInitiationDto)))
        .andExpect(status().isOk());

    Optional<PasswordResetVerificationToken> passwordResetVerificationToken =
        passwordResetVerificationTokenRepository.findByUser(
            usersRepository.findByUsername("admin@myCompany.net").get());

    assertThat(passwordResetVerificationToken.isPresent()).isTrue();

    verify(notificationService, times(1))
        .sendPasswordResetVerificationEmail(
            "admin@myCompany.net", passwordResetVerificationToken.get().getToken());

    PasswordResetDto passwordResetDto =
        new PasswordResetDto(
            "newPassword1@", "newPassword1@", passwordResetVerificationToken.get().getToken());

    mockMvc
        .perform(
            post("/users/password/change")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(passwordResetDto)))
        .andDo(print())
        .andExpect(status().isOk());

    Optional<User> user = usersRepository.findByUsername("admin@myCompany.net");

    assertThat(user.get().getPassword()).isEqualTo("encodedNewPassword");
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPasswords")
  @DisplayName("Test invalid passwords complexity for registration")
  public void testPasswordComplexity(String password) throws Exception {

    UserRegistrationDto userRegistrationDto =
        new UserRegistrationDto(
            password, password, "firstName", "lastName", "email@example.com", "Dummy Inc");

    mockMvc
        .perform(
            post("/users/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRegistrationDto)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test password not matching for registration")
  public void testPasswordMatch() throws Exception {

    UserRegistrationDto userRegistrationDto =
        new UserRegistrationDto(
            "password", "noMatch", "firstName", "lastName", "email@example.com", "Dummy Inc");

    mockMvc
        .perform(
            post("/users/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRegistrationDto)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Upload users endpoint with non admin role should return forbidden")
  public void testUploadUsersEndpointFailure() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            "George,Andrinopoulos,george@me.com\nStella,Kafetzoglou,stella@me.com".getBytes());

    mockMvc
        .perform(
            multipart("/users/upload")
                .file(file)
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "myCompany", Set.of("USER")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Upload users endpoint with admin role should create users")
  public void testUploadUsersEndpointSuccess() throws Exception {
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            "George,Andrinopoulos,george@me.com\nStella,Kafetzoglou,stella@me.com".getBytes());

    mockMvc
        .perform(
            multipart("/users/upload")
                .file(file)
                .with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(JwtBuilder.jwt("test", "myCompany2", Set.of("ADMIN")))
                        .authorities(customJwtAuthenticationConverter)))
        .andExpect(status().isOk());

    User user1 = usersRepository.findByUsername("george@me.com").orElseThrow(RuntimeException::new);
    assertThat(user1).isNotNull();
    assertThat(user1.getFirstName()).isEqualTo("George");
    assertThat(user1.getLastName()).isEqualTo("Andrinopoulos");
    assertThat(user1.getEmail()).isEqualTo("george@me.com");
    assertThat(user1.getCompany().getName()).isEqualTo("myCompany2");
    assertThat(user1.isCredentialsNonExpired()).isEqualTo(false);

    User user2 = usersRepository.findByUsername("stella@me.com").orElseThrow(RuntimeException::new);
    assertThat(user2).isNotNull();
    assertThat(user2.getFirstName()).isEqualTo("Stella");
    assertThat(user2.getLastName()).isEqualTo("Kafetzoglou");
    assertThat(user2.getEmail()).isEqualTo("stella@me.com");
    assertThat(user2.getCompany().getName()).isEqualTo("myCompany2");
    assertThat(user2.isCredentialsNonExpired()).isEqualTo(false);
  }

  private static String[] provideInvalidPasswords() {
    return new String[] {
      "12345678", "abcdefgh", "ABCDEFGH", "!2fsdfsda", "12345678A", "1234567@4dsa",
    };
  }
}
