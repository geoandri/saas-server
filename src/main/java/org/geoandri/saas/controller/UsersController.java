package org.geoandri.saas.controller;

import static org.geoandri.saas.constants.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static org.geoandri.saas.constants.PaginationConstants.DEFAULT_PAGE_SIZE;

import jakarta.validation.Valid;
import java.util.UUID;
import org.geoandri.saas.dto.request.PasswordResetDto;
import org.geoandri.saas.dto.request.PasswordResetInitiationDto;
import org.geoandri.saas.dto.request.UserRegistrationDto;
import org.geoandri.saas.dto.response.UserPageResponseDto;
import org.geoandri.saas.dto.response.UserResponseDto;
import org.geoandri.saas.service.UsersService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UsersController {

  private final UsersService usersService;

  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('ADMIN')")
  public UserPageResponseDto getUsers(
      @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int pageNumber,
      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
      @AuthenticationPrincipal(expression = "getClaims().get('company')") String company) {
    return usersService.getAllUsers(company, PageRequest.of(pageNumber, pageSize));
  }

  @PostMapping("/registration")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
    return usersService.registerUser(userRegistrationDto);
  }

  @GetMapping("/verify")
  @ResponseStatus(HttpStatus.OK)
  public String verifyUser(@RequestParam(name = "token") UUID token) {
    usersService.verifyUser(token);

    return "User verified successfully";
  }

  @PostMapping("/password/reset")
  @ResponseStatus(HttpStatus.OK)
  public String passwordResetInitiate(
      @RequestBody @Valid PasswordResetInitiationDto passwordResetInitiationDto) {
    usersService.passwordResetInitiate(passwordResetInitiationDto.email());

    return "Password reset request sent successfully";
  }

  @PostMapping("/password/change")
  @ResponseStatus(HttpStatus.OK)
  public String passwordReset(@RequestBody @Valid PasswordResetDto passwordResetDto) {
    usersService.passwordReset(passwordResetDto.password(), passwordResetDto.token());

    return "Password reset was successful";
  }

  @PostMapping("/upload")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('ADMIN')")
  public String uploadUsers(
      @RequestParam MultipartFile file,
      @AuthenticationPrincipal(expression = "getClaims().get('company')") String company) {
    usersService.uploadUsers(file, company);

    return "Users uploaded successfully";
  }
}
