package org.geoandri.saas.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-auth")
public class DummyController {

  @GetMapping("/super-admin")
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public String testSuperAdmin() {
    return "test Super Admin success";
  }

  @GetMapping("/user")
  @PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER_ADMIN')")
  public String testUser() {
    return "test User success";
  }
}
