package org.geoandri.saas.repository;

import java.util.Optional;
import java.util.UUID;
import org.geoandri.saas.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompaniesRepository extends JpaRepository<Company, UUID> {

  Optional<Company> findByName(String name);
}
