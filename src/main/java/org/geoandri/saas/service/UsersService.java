package org.geoandri.saas.service;

import static org.geoandri.saas.mapper.UserPageResponseDtoMapper.mapToUserPageResponseDto;

import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.geoandri.saas.dto.request.UploadedUserDto;
import org.geoandri.saas.dto.request.UserRegistrationDto;
import org.geoandri.saas.dto.response.UserPageResponseDto;
import org.geoandri.saas.dto.response.UserResponseDto;
import org.geoandri.saas.entity.Authority;
import org.geoandri.saas.entity.Company;
import org.geoandri.saas.entity.PasswordResetVerificationToken;
import org.geoandri.saas.entity.RegistrationVerificationToken;
import org.geoandri.saas.entity.User;
import org.geoandri.saas.entity.UserRole;
import org.geoandri.saas.exception.CompanyNotFoundException;
import org.geoandri.saas.repository.AuthoritiesRepository;
import org.geoandri.saas.repository.CompaniesRepository;
import org.geoandri.saas.repository.PasswordResetVerificationTokenRepository;
import org.geoandri.saas.repository.RegistrationVerificationTokenRepository;
import org.geoandri.saas.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UsersService {

  private final UsersRepository usersRepository;

  private final CompaniesRepository companiesRepository;

  private final AuthoritiesRepository authoritiesRepository;

  private final RegistrationVerificationTokenRepository registrationVerificationTokenRepository;

  private final PasswordResetVerificationTokenRepository passwordResetVerificationTokenRepository;

  private final NotificationService notificationService;

  private final PasswordEncoder passwordEncoder;

  public UsersService(
      UsersRepository usersRepository,
      CompaniesRepository companiesRepository,
      AuthoritiesRepository authoritiesRepository,
      RegistrationVerificationTokenRepository registrationVerificationTokenRepository,
      PasswordResetVerificationTokenRepository passwordResetVerificationTokenRepository,
      NotificationService notificationService,
      PasswordEncoder passwordEncoder) {
    this.usersRepository = usersRepository;
    this.companiesRepository = companiesRepository;
    this.authoritiesRepository = authoritiesRepository;
    this.registrationVerificationTokenRepository = registrationVerificationTokenRepository;
    this.passwordResetVerificationTokenRepository = passwordResetVerificationTokenRepository;
    this.notificationService = notificationService;
    this.passwordEncoder = passwordEncoder;
  }

  public UserPageResponseDto getAllUsers(String company, PageRequest pageRequest) {
    Company persistedCompany =
        companiesRepository
            .findByName(company)
            .orElseThrow(() -> new CompanyNotFoundException("Company not found"));
    Page<User> userPage = usersRepository.findAllByCompany(persistedCompany, pageRequest);

    return mapToUserPageResponseDto(userPage);
  }

  public UserResponseDto registerUser(UserRegistrationDto userRegistrationDto) {
    Company company = new Company();
    company.setId(UUID.randomUUID());
    company.setName(userRegistrationDto.company());

    companiesRepository.save(company);

    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername(userRegistrationDto.email());
    user.setPassword(passwordEncoder.encode(userRegistrationDto.password()));
    user.setFirstName(userRegistrationDto.firstName());
    user.setLastName(userRegistrationDto.lastName());
    user.setEmail(userRegistrationDto.email());
    user.setCompany(company);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(false);
    usersRepository.save(user);

    Authority registeredAuthority = new Authority();
    registeredAuthority.setUsername(userRegistrationDto.email());
    registeredAuthority.setUserRole(UserRole.ADMIN);
    authoritiesRepository.save(registeredAuthority);

    user.setAuthorities(Set.of(registeredAuthority));

    RegistrationVerificationToken registrationVerificationToken =
        new RegistrationVerificationToken(UUID.randomUUID(), user);
    registrationVerificationTokenRepository.save(registrationVerificationToken);

    notificationService.sendRegistrationVerificationEmail(
        userRegistrationDto.email(), registrationVerificationToken.getToken());

    return new UserResponseDto(
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getCompany().getName(),
        user.getAuthorities().stream()
            .map(authority -> authority.getUserRole())
            .collect(Collectors.toSet()));
  }

  public void verifyUser(UUID token) {
    RegistrationVerificationToken registrationVerificationToken =
        registrationVerificationTokenRepository
            .findById(token)
            .orElseThrow(() -> new RuntimeException("Registration token not found"));

    if (registrationVerificationToken.isTokenExpired()) {
      throw new RuntimeException("Registration token has expired");
    }

    User user = registrationVerificationToken.getUser();
    user.setEnabled(true);
    usersRepository.save(user);
  }

  public void passwordResetInitiate(String email) {
    User user = usersRepository.findByUsername(email).get();
    PasswordResetVerificationToken passwordResetVerificationToken =
        new PasswordResetVerificationToken(UUID.randomUUID(), user);
    passwordResetVerificationTokenRepository.save(passwordResetVerificationToken);

    notificationService.sendPasswordResetVerificationEmail(
        email, passwordResetVerificationToken.getToken());
  }

  public void passwordReset(String password, UUID token) {
    PasswordResetVerificationToken passwordResetVerificationToken =
        passwordResetVerificationTokenRepository
            .findById(token)
            .orElseThrow(() -> new RuntimeException("Registration token not found"));

    if (passwordResetVerificationToken.isTokenExpired()) {
      throw new RuntimeException("Registration token has expired");
    }

    User user = passwordResetVerificationToken.getUser();
    user.setPassword(passwordEncoder.encode(password));
    usersRepository.save(user);
  }

  public void uploadUsers(MultipartFile file, String company) {
    Company persistedCompany =
        companiesRepository
            .findByName(company)
            .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

    parseCsvAndPersistUsers(file, persistedCompany);
  }

  private void parseCsvAndPersistUsers(MultipartFile file, Company persistedCompany) {
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

      String line;
      while ((line = ((BufferedReader) reader).readLine()) != null) {
        String[] data = line.split(",");
        UploadedUserDto userDto = new UploadedUserDto(data[0], data[1], data[2]);

        User user = new User();
        user.setUsername(userDto.email());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setId(UUID.randomUUID());
        user.setCompany(persistedCompany);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(false);
        user.setEnabled(true);

        usersRepository.save(user);

        Authority registeredAuthority = new Authority();
        registeredAuthority.setUsername(user.getUsername());
        registeredAuthority.setUserRole(UserRole.USER);
        authoritiesRepository.save(registeredAuthority);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
