package org.geoandri.saas.repository;

import java.util.Optional;
import java.util.UUID;
import org.geoandri.saas.entity.RegistrationVerificationToken;
import org.geoandri.saas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationVerificationTokenRepository
    extends JpaRepository<RegistrationVerificationToken, UUID> {

  Optional<RegistrationVerificationToken> findByUser(User user);
}
