package org.geoandri.saas.repository;

import java.util.Optional;
import java.util.UUID;
import org.geoandri.saas.entity.PasswordResetVerificationToken;
import org.geoandri.saas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetVerificationTokenRepository
    extends JpaRepository<PasswordResetVerificationToken, UUID> {

  Optional<PasswordResetVerificationToken> findByUser(User user);
}
