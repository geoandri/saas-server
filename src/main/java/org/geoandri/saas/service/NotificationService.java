package org.geoandri.saas.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

  @Async
  public void sendRegistrationVerificationEmail(String email, UUID token) {
    LOGGER.info("Sending email to: " + email);
    LOGGER.info(
        "Visit this link to activate your account: http://localhost:8080/users/verify?token="
            + token);
  }

  @Async
  public void sendPasswordResetVerificationEmail(String email, UUID token) {
    LOGGER.info("Sending email to: " + email);
    LOGGER.info(
        "Visit this link to reset your password: http://localhost:8080/users/password/reset?token="
            + token);
  }
}
