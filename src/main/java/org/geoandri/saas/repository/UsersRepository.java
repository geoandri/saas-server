package org.geoandri.saas.repository;

import java.util.Optional;
import java.util.UUID;
import org.geoandri.saas.entity.Company;
import org.geoandri.saas.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  Page<User> findAllByCompany(Company company, Pageable pageable);
}
