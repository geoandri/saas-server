package org.geoandri.saas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geoandri.saas.configuration.CustomJwtAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTests {

  @Autowired private WebApplicationContext context;

  @Autowired CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

  @Autowired protected ObjectMapper objectMapper;

  protected MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
  }
}
