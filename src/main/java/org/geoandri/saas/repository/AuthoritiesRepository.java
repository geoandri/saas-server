package org.geoandri.saas.repository;

import org.geoandri.saas.entity.Authority;
import org.geoandri.saas.entity.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authority, AuthorityId> {}
